package com.hangangnow.openapiserver.controller;

import com.hangangnow.openapiserver.service.ParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @GetMapping("/api/v1/parkings")
    public void parkingCrawler() throws IOException {
        parkingService.requestParking();
    }

}
