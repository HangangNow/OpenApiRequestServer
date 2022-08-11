package com.hangangnow.openapiserver.service;

import com.hangangnow.openapiserver.domain.*;
import com.hangangnow.openapiserver.domain.dto.ParkPhotoRequestDto;
import com.hangangnow.openapiserver.domain.dto.ParkRequestDto;
import com.hangangnow.openapiserver.domain.dto.ParkingRequestDto;
import com.hangangnow.openapiserver.repository.ParkRepository;
import com.hangangnow.openapiserver.repository.ParkingRepository;
import com.hangangnow.openapiserver.repository.PhotoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@PropertySource("classpath:/application-secret.properties")
public class ParkService {

    @Value("${tourApiKey}")
    private String key;


    private final ParkRepository parkRepository;
    private final PhotoRepository photoRepository;

    @Scheduled(cron = "0 0/2 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void requestPark()  throws IOException, JDOMException {
        LocalDateTime now = LocalDateTime.now();
        log.info(now + " 국문관광정보 공통정보조회 API 호출");
        // 광나루, 잠실, 뚝섬, 잠원, 반포, 이촌, 망원, 여의도, 난지, 강서, 양화
        String[] contentIds = {"250252", "970460", "1030763", "970342", "970138", "970636",
                "1059638", "1059479", "127859", "2733968", "1059877"};

        List<ParkRequestDto> parkRequestDtos = new ArrayList<>();

        for (String contentId : contentIds) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService/detailCommon"); /*URL*/
            urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=").append(key); /*Service Key*/
            urlBuilder.append("&").append(URLEncoder.encode("_type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("XML", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
//            urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*현재 페이지 번호*/
            urlBuilder.append("&").append(URLEncoder.encode("MobileOS", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("ETC", StandardCharsets.UTF_8)); /*IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC*/
            urlBuilder.append("&").append(URLEncoder.encode("MobileApp", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("HangangNOW", StandardCharsets.UTF_8)); /*서비스명=어플명*/
            urlBuilder.append("&").append(URLEncoder.encode("contentId", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(contentId, StandardCharsets.UTF_8)); /*콘텐츠ID*/
            urlBuilder.append("&").append(URLEncoder.encode("contentTypeId", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("", StandardCharsets.UTF_8)); /*관광타입(관광지, 숙박 등) ID*/
            urlBuilder.append("&").append(URLEncoder.encode("defaultYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*기본정보 조회여부*/
            urlBuilder.append("&").append(URLEncoder.encode("firstImageYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*원본, 썸네일 대표이미지 조회여부*/
            urlBuilder.append("&").append(URLEncoder.encode("areacodeYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*지역코드, 시군구코드 조회여부*/
            urlBuilder.append("&").append(URLEncoder.encode("catcodeYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*대,중,소분류코드 조회여부*/
            urlBuilder.append("&").append(URLEncoder.encode("addrinfoYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*주소, 상세주소 조회여부*/
            urlBuilder.append("&").append(URLEncoder.encode("mapinfoYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*좌표 X,Y 조회여부*/
            urlBuilder.append("&").append(URLEncoder.encode("overviewYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8)); /*콘텐츠 개요 조회여부*/
            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            System.out.println("conn = " + conn);
            log.info("Response code: " + conn.getResponseCode());

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(conn.getInputStream());

            Element root = document.getRootElement();
            Element body = root.getChild("body");
            Element items = body.getChild("items");
            List<Element> item = items.getChildren("item");

            for (Element element : item) {
                String parkName = element.getChild("title").getText();
                String addr1 = element.getChild("addr1").getText();
                String[] addresses = addr1.split("\\s");
                String si = addresses[0];
                String gu = addresses[1];
                String detail = "";

                for(int i=2; i< addresses.length; i++){
                    detail += addresses[i] + " ";
                }

                Double x_pos = Double.valueOf(element.getChild("mapx").getText());
                Double y_pos = Double.valueOf(element.getChild("mapy").getText());
                String contentOrigin = element.getChild("overview").getText();
                String contentModify = contentOrigin.replaceAll("<br>", " ");
                String content = contentModify.replaceAll("<br />", " ");

                ParkRequestDto parkRequestDto = new ParkRequestDto(contentId, parkName, si, gu, detail, x_pos, y_pos, content, LocalDateTime.now());

                parkRequestDtos.add(parkRequestDto);

            }

            conn.disconnect();

        }

        updateParkInfo(parkRequestDtos);

    }


    public void createParkInfo(List<ParkRequestDto> parkRequestDtos){
        for (ParkRequestDto parkRequestDto : parkRequestDtos) {
            Local local = new Local(parkRequestDto.getParkName(), parkRequestDto.getX_pos(), parkRequestDto.getY_pos());
            Address address = new Address(parkRequestDto.getSi(), parkRequestDto.getGu(), parkRequestDto.getDetail());

            Park park = new Park(parkRequestDto.getContentId(), parkRequestDto.getParkName(), local, address, parkRequestDto.getContent(), parkRequestDto.getLastModifiedTime());
            parkRepository.save(park);
        }
    }


    public void updateParkInfo(List<ParkRequestDto> parkRequestDtos){
        for (ParkRequestDto parkRequestDto : parkRequestDtos) {
            Park findPark = parkRepository.findByContentId(parkRequestDto.getContentId());
            Local local = new Local(parkRequestDto.getParkName(), parkRequestDto.getX_pos(), parkRequestDto.getY_pos());
            Address address = new Address(parkRequestDto.getSi(), parkRequestDto.getGu(), parkRequestDto.getDetail());
            findPark.updatePark(parkRequestDto.getParkName(), local, address, parkRequestDto.getContent(), parkRequestDto.getLastModifiedTime());
        }
    }

    @Scheduled(cron = "0 0/3 * * * *", zone = "Asia/Seoul")
    @Transactional
    public void requestParkImage() throws IOException, JDOMException {
        LocalDateTime now = LocalDateTime.now();
        log.info(now + " 국문관광정보 이미지 정보 API 호출");

        // 광나루, 잠실, 뚝섬, 잠원, 반포, 이촌, 망원, 여의도, 난지, 강서, 양화
        String[] contentIds = {"250252", "970460", "1030763", "970342", "970138", "970636",
                "1059638", "1059479", "127859", "2733968", "1059877"};

        List<ParkPhotoRequestDto> parkPhotoRequestDtos = new ArrayList<>();

        for (String contentId : contentIds) {
            StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B551011/KorService/detailImage"); /*URL*/
            urlBuilder.append("?").append(URLEncoder.encode("serviceKey", StandardCharsets.UTF_8)).append("=").append(key); /*Service Key*/
            urlBuilder.append("&").append(URLEncoder.encode("_type", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("XML", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
            urlBuilder.append("&").append(URLEncoder.encode("numOfRows", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("50", StandardCharsets.UTF_8)); /*한 페이지 결과 수*/
            urlBuilder.append("&").append(URLEncoder.encode("pageNo", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("1", StandardCharsets.UTF_8)); /*현재 페이지 번호*/
            urlBuilder.append("&").append(URLEncoder.encode("MobileOS", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("ETC", StandardCharsets.UTF_8)); /*IOS(아이폰),AND(안드로이드),WIN(원도우폰),ETC*/
            urlBuilder.append("&").append(URLEncoder.encode("MobileApp", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("HangangNOW", StandardCharsets.UTF_8)); /*서비스명=어플명*/
            urlBuilder.append("&").append(URLEncoder.encode("contentId", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(contentId, StandardCharsets.UTF_8)); /*콘텐츠ID*/
            urlBuilder.append("&").append(URLEncoder.encode("imageYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8));
            urlBuilder.append("&").append(URLEncoder.encode("subImageYN", StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode("Y", StandardCharsets.UTF_8));

            URL url = new URL(urlBuilder.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            log.info("Response code: " + conn.getResponseCode());

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(conn.getInputStream());

            Element root = document.getRootElement();
            Element body = root.getChild("body");
            Element items = body.getChild("items");
            List<Element> item = items.getChildren("item");

            HashMap<String, String> hashMap = new HashMap<>();
            for (Element element : item) {
                String originImgUrl = element.getChild("originimgurl").getText();
                String imageNumber = element.getChild("serialnum").getText();
                hashMap.put(imageNumber, originImgUrl);
            }

            ParkPhotoRequestDto parkPhotoRequestDto = new ParkPhotoRequestDto(contentId, hashMap);
            parkPhotoRequestDtos.add(parkPhotoRequestDto);

            conn.disconnect();
        }

        updateParkPhoto(parkPhotoRequestDtos);
    }



    public void createParkPhoto(List<ParkPhotoRequestDto> parkPhotoRequestDtos){
        for (ParkPhotoRequestDto parkPhotoRequestDto : parkPhotoRequestDtos) {
            Park findPark = parkRepository.findByContentId(parkPhotoRequestDto.getContentId());

            HashMap<String, String> hashMap = parkPhotoRequestDto.getHashMap();
            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                ParkPhoto parkPhoto = new ParkPhoto(stringStringEntry.getKey(), stringStringEntry.getValue(), LocalDateTime.now());
                findPark.addPhoto(parkPhoto);
            }
        }
    }


    public void updateParkPhoto(List<ParkPhotoRequestDto> parkPhotoRequestDtos){
        for (ParkPhotoRequestDto parkPhotoRequestDto : parkPhotoRequestDtos) {
            HashMap<String, String> hashMap = parkPhotoRequestDto.getHashMap();
            for (Map.Entry<String, String> stringStringEntry : hashMap.entrySet()) {
                String imageNumber = stringStringEntry.getKey();
                ParkPhoto findPhoto = photoRepository.findByImageNumber(imageNumber)
                        .orElse(new ParkPhoto());

                if(findPhoto.getImageNumber() == null){
                    Park findPark = parkRepository.findByContentId(parkPhotoRequestDto.getContentId());
                    ParkPhoto photo = new ParkPhoto(imageNumber, stringStringEntry.getValue(), LocalDateTime.now());
                    findPark.addPhoto(photo);
                }
                else{
                    findPhoto.updateParkPhoto(stringStringEntry.getValue(), LocalDateTime.now());
                }
            }
        }
    }

}
