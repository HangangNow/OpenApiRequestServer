package com.hangangnow.openapiserver.controller;

import com.hangangnow.openapiserver.domain.dto.ParkingRequestDto;
import com.hangangnow.openapiserver.service.ParkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParkingController {

    private final ParkService parkService;

    private static String URL = "https://www.ihangangpark.kr/parking/region/";
    private static String region = "region";

//    @Scheduled(cron = "0 0/2 * * * *", zone = "Asia/Seoul")
    @GetMapping("/api/v1/parking/info")
    public void parkingCrawler() throws IOException {

        log.info("한강공원 주차장 크롤러 동작.");

        // 광나루, 잠실, 뚝섬, 잠원, 반포, 이촌, 망원, 여의도, 난지, 강서, 양화
        String[] contentIds = {"250252", "970460", "1030763", "970342", "970138", "970636",
                "1059638", "1059479", "127859", "2733968", "1059877"};

        String[] parkNames = {"광나루한강공원", "잠실한강공원", "뚝섬한강공원", "잠원한강공원", "반포한강공원", "이촌한강공원",
                "망원한강공원", "여의도한강공원", "난지한강공원", "강서한강공원", "양화한강공원"};

        List<ParkingRequestDto> parkingRequestDtos = new ArrayList<>();

        for(int idx=1; idx<12; idx++)
        {
            Document doc = Jsoup.connect(URL+region+idx).get();
            Elements eles = doc.select(".region-tab-cont.active .table.board.mt10 tbody tr td");

            for(int i=0; i<eles.size(); i+=6)
            {
                String name = Jsoup.parse(eles.get(i).toString()).text();
                String address = Jsoup.parse(eles.get(i+1).toString()).text();
                String[] addresses = address.split("\\s");
                String si = addresses[0];
                String gu = addresses[1];
                String gil = addresses[2];
                String detail = "";

                for(int j=3; j< addresses.length; j++){
                    detail += addresses[j] + " ";
                }

                Integer avail = Integer.parseInt(Jsoup.parse(eles.get(i+3).toString()).text());
                Integer total = Integer.parseInt(Jsoup.parse(eles.get(i+4).toString()).text());

                ParkingRequestDto parkingRequestDto = new ParkingRequestDto(Long.parseLong(contentIds[idx-1]), parkNames[idx-1], si, gu, gil, detail, name, avail, total);
                parkingRequestDtos.add(parkingRequestDto);
                System.out.println("parkIds = " + contentIds[idx-1]);
                System.out.println("parkNames = " + parkNames[idx-1]);
                System.out.println("name = " + name);
                System.out.println("address = " + address);
                System.out.println("avail = " + avail);
                System.out.println("total = " + total);

            }
            System.out.println("================================================");

        }

        parkService.updateParkingInfo(parkingRequestDtos);

    }

}
