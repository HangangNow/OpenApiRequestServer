package com.hangangnow.openapiserver.controller;

import com.hangangnow.openapiserver.service.ParkService;
import lombok.RequiredArgsConstructor;

import org.jdom2.JDOMException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@RequiredArgsConstructor
@RestController
public class ParkController {

    private final ParkService parkService;


    @GetMapping("/api/v1/park/detailCommon")
    public ResponseEntity<String> commonInfoApi() throws IOException, JDOMException {
        parkService.requestPark();
        return new ResponseEntity<>("한국관광공사_국문 관광정보 서비스 공통정보조회 API 호출", HttpStatus.OK);
    }



    @GetMapping("/api/v1/park/detailImage")
    public ResponseEntity<String> imageInfoApi() throws IOException, JDOMException {
        parkService.requestParkImage();
        return new ResponseEntity<>("한국관광공사_국문 관광정보 서비스 이미지 정보조회 API 호출", HttpStatus.OK);
    }

}