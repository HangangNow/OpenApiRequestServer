package com.hangangnow.openapiserver.controller;

import com.hangangnow.openapiserver.domain.dto.ParkingRequestDto;
import com.hangangnow.openapiserver.service.ParkService;
import com.hangangnow.openapiserver.service.ParkingService;
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

    private final ParkingService parkingService;


//    @Scheduled(cron = "0 0/2 * * * *", zone = "Asia/Seoul")
    @GetMapping("/api/v1/parking/info")
    public void parkingCrawler() throws IOException {
        parkingService.requestParking();
    }

}
