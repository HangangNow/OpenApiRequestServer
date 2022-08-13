package com.hangangnow.openapiserver.service;

import com.hangangnow.openapiserver.domain.hangangnow.Dust;
import com.hangangnow.openapiserver.domain.hangangnow.SunMoonRiseSet;
import com.hangangnow.openapiserver.domain.hangangnow.Weather;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@PropertySource("classpath:/application-secret.properties")
public class HangangNowRequestService {

    @Value("${tourWeatherApiKey}")
    private String tourWeatherApiKey;
    @Value("${dustApiKey}")
    private String dustApiKey;
    @Value("${riseSetApiKey}")
    private String riseSetApiKey;
    @Value("${weatherApiKey}")
    private String weatherApiKey;

    public Weather requestWeather() throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/TourStnInfoService/getTourStnVilageFcst");
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
        String Courese_Id = "58";

        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + tourWeatherApiKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("CURRENT_DATE", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("HOUR", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("COURSE_ID", "UTF-8") + "=" + URLEncoder.encode(Courese_Id, "UTF-8"));
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.info("기상청_관광코스별 관광지 상세 날씨 조회서비스 API");
        log.info("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            System.out.println(line);
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
//        System.out.println(sb.toString());

        JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());
        JSONObject response = (JSONObject) result.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");
        List<JSONObject> itemList = item;
        Collections.reverse(itemList);
        Integer maxPop = 0;
        List<Integer> skyList = Arrays.asList(0,0,0,0,0,0,0,0,0);

        for (JSONObject jsonObject : itemList) {
            if(jsonObject.get("spotAreaId").toString().equals("5803")){
//                StringBuilder out = new StringBuilder();
//                out.append("온도 : "+jsonObject.get("th3")+" ");
//                out.append("시각 : "+jsonObject.get("tm")+" ");
//                out.append("하늘 : " +jsonObject.get("sky")+" "); // 1: 맑음, 2: 구름, 3: 흐림, 4: 비, 5: 소나기, 6: 비눈, 7: 눈비, 8: 눈
//                out.append("습도 : " +jsonObject.get("rhm")+" ");
//                out.append("강수확률 : " +jsonObject.get("pop")+"\n");
//                System.out.println(out);
                Integer skyIdx = Integer.parseInt(jsonObject.get("sky").toString());
                Integer skyVal = skyList.get(skyIdx);
                skyList.set(skyIdx, skyVal+1);
                if(Integer.parseInt(jsonObject.get("pop").toString()) > maxPop){
                    maxPop = Integer.parseInt(jsonObject.get("pop").toString());
                }
            }
        }
        System.out.println("max 강수확률: " + maxPop + " 하늘상태 최빈값: " + skyList.indexOf(Collections.max(skyList)));

        return new Weather(skyList.indexOf(Collections.max(skyList)), maxPop);
    }

    public Optional<Dust> requestDust() throws IOException, ParseException {
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/getCtprvnRltmMesureDnsty"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + dustApiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("returnType","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*xml 또는 json*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("100", "UTF-8")); /*한 페이지 결과 수(조회 날짜로 검색 시 사용 안함)*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호(조회 날짜로 검색 시 사용 안함)*/
        urlBuilder.append("&" + URLEncoder.encode("sidoName","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*시도 이름(전국, 서울, 부산, 대구, 인천, 광주, 대전, 울산, 경기, 강원, 충북, 충남, 전북, 전남, 경북, 경남, 제주, 세종)*/
        urlBuilder.append("&" + URLEncoder.encode("ver","UTF-8") + "=" + URLEncoder.encode("1.1", "UTF-8")); /*버전별 상세 결과 참고*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.info("한국환경공단_에어코리아_대기오염정보 API");
        log.info("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
//        System.out.println(sb.toString());

        JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());
        JSONObject response = (JSONObject) result.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONArray items = (JSONArray) body.get("items");
        List<JSONObject> itemList = items;
        Dust dust = null;
        for (JSONObject jsonObject : itemList) {
            System.out.println("jsonObject.get(\"stationName\").toString() = " + jsonObject.get("stationName").toString());
            if(jsonObject.get("stationName").toString().equals("용산구")){
                dust = new Dust(Integer.parseInt(jsonObject.get("pm25Grade").toString()),
                        Integer.parseInt(jsonObject.get("pm10Grade").toString()));
                StringBuilder out = new StringBuilder();
                out.append("초미세먼지 : "+jsonObject.get("pm25Grade")+" ");
                out.append("미세먼지 : "+jsonObject.get("pm10Grade")+"\n");
                System.out.println(out);
                break;
            }
        }

        return Optional.ofNullable(dust);
    }

    public SunMoonRiseSet requestSunMoonRiseSet() throws IOException, JDOMException {
        LocalDate currentDate = LocalDate.now();
        String date = currentDate.format(DateTimeFormatter.BASIC_ISO_DATE);

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/getAreaRiseSetInfo"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + riseSetApiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("locdate","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*날짜*/
        urlBuilder.append("&" + URLEncoder.encode("location","UTF-8") + "=" + URLEncoder.encode("서울", "UTF-8")); /*지역*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.info("한국천문연구원_출몰시각 정보 API");
        log.info("Response code: " + conn.getResponseCode());

        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(conn.getInputStream());

        Element root = document.getRootElement();
        Element body = root.getChild("body");
        Element items = body.getChild("items");
        List<Element> item = items.getChildren("item");

        String sunRise = item.get(0).getChild("sunrise").getText().strip();
        String sunSet = item.get(0).getChild("sunset").getText().strip();
        String moonRise = item.get(0).getChild("moonrise").getText().strip();
        sunRise = sunRise.substring(0,2) + ":" + sunRise.substring(2);
        sunSet = sunSet.substring(0,2) + ":" + sunSet.substring(2);
        moonRise = moonRise.substring(0,2) + ":" + moonRise.substring(2);


        System.out.println("Date: " + date+ " sunrise: " + sunRise + " sunset: " + sunSet + " moonRise: " + moonRise);

        conn.disconnect();
        return new SunMoonRiseSet(sunRise, sunSet, moonRise);
    }

    public Optional<Double> requestTemperature() throws IOException, ParseException{
        LocalDateTime currentTime = LocalDateTime.now();
        if(currentTime.getMinute()<30) currentTime = currentTime.minusHours(1);
        String date = currentTime.format(DateTimeFormatter.BASIC_ISO_DATE);
        String time = currentTime.format(DateTimeFormatter.ofPattern("HHmm"));

        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtNcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + weatherApiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType","UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date","UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time","UTF-8") + "=" + URLEncoder.encode(time, "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx","UTF-8") + "=" + URLEncoder.encode("60", "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny","UTF-8") + "=" + URLEncoder.encode("127", "UTF-8")); /*예보지점의 Y 좌표값*/
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        log.info("기상청_단기예보 ((구)_동네예보) 조회서비스 API");
        log.info("Response code: " + conn.getResponseCode());

        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
//        System.out.println(sb.toString());

        JSONObject result = (JSONObject) new JSONParser().parse(sb.toString());
        JSONObject response = (JSONObject) result.get("response");
        JSONObject body = (JSONObject) response.get("body");
        JSONObject items = (JSONObject) body.get("items");
        JSONArray item = (JSONArray) items.get("item");
        List<JSONObject> itemList = item;
        Double temperature = null;

        for (JSONObject jsonObject : itemList) {
            if(jsonObject.get("category").toString().equals("T1H")){
                StringBuilder out = new StringBuilder();
                out.append("온도 : "+jsonObject.get("obsrValue")+"\n");
                temperature = Double.parseDouble(jsonObject.get("obsrValue").toString());
                System.out.println(out);
                break;
            }
        }
        return Optional.ofNullable(temperature);
    }
}
