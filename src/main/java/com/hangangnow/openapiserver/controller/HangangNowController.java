package com.hangangnow.openapiserver.controller;

import com.hangangnow.openapiserver.domain.hangangnow.Dust;
import com.hangangnow.openapiserver.domain.hangangnow.HangangNowData;
import com.hangangnow.openapiserver.domain.hangangnow.SunRiseSunSet;
import com.hangangnow.openapiserver.domain.hangangnow.Weather;
import com.hangangnow.openapiserver.service.HangangNowService;
import lombok.RequiredArgsConstructor;
import org.jdom2.JDOMException;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hangangnow")
public class HangangNowController {
    private final HangangNowService hangangNowService;

    @GetMapping("")
    public HangangNowData HND(){
        return hangangNowService.findHND();
    }

    @PostMapping("/init")
    public HangangNowData init() throws IOException, ParseException, JDOMException {
        return hangangNowService.init();
    }

    @PutMapping("/weather")
    public Weather CallWeatherApi() throws IOException, ParseException {
        return hangangNowService.updateWeather();
    }

    @PutMapping("/dust")
    public Dust CallApiDust() throws IOException, ParseException {
        return hangangNowService.updateDust();
    }

    @PutMapping("/sunrisesunset")
    public SunRiseSunSet CallApiSunRiseSunSet() throws IOException, JDOMException {
        return hangangNowService.updateSunRiseSunSet(LocalDate.now());
    }

    @PutMapping("/temperature")
    public Double CallApiTemperature() throws IOException, ParseException{
        return hangangNowService.updateTemperature();
    }

}
