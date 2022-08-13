package com.hangangnow.openapiserver.service;

import com.hangangnow.openapiserver.domain.hangangnow.Dust;
import com.hangangnow.openapiserver.domain.hangangnow.HangangNowData;
import com.hangangnow.openapiserver.domain.hangangnow.SunMoonRiseSet;
import com.hangangnow.openapiserver.domain.hangangnow.Weather;
import com.hangangnow.openapiserver.repository.HangangNowRepository;
import lombok.RequiredArgsConstructor;
import org.jdom2.JDOMException;
import org.json.simple.parser.ParseException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class HangangNowService {
    private final HangangNowRepository hangangNowRepository;
    private final HangangNowRequestService HNRS;

    public HangangNowData findHND(){
        return hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not Found HangangNowData"));
    }

    public HangangNowData init() throws IOException, ParseException, JDOMException {
        Double temperature = HNRS.requestTemperature()
                .orElseThrow(() -> new NullPointerException("Failed: The request temperature data does not have a value."));
        Dust dust = HNRS.requestDust()
                .orElseThrow(() -> new NullPointerException("Failed: The request dust data does not have a value."));
        Weather weather = HNRS.requestWeather();
        SunMoonRiseSet sunMoonRiseSet = HNRS.requestSunMoonRiseSet();

        HangangNowData initHangangNowData = new HangangNowData(
                1L,
                LocalDateTime.now(),
                temperature,
                weather,
                dust,
                sunMoonRiseSet
        );
        hangangNowRepository.save(initHangangNowData);
        return initHangangNowData;
    }

    @Scheduled(cron = "0 10 23 * * *", zone = "Asia/Seoul")
    public Weather updateWeather() throws IOException, ParseException {
        HangangNowData hangangNowData = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        Weather weather = HNRS.requestWeather();
        hangangNowData.update(weather);
        return weather;
    }

    @Scheduled(cron = "0 3/10 * * * *", zone = "Asia/Seoul")
    public Double updateTemperature() throws IOException, ParseException {
        HangangNowData hangangNowData = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        Double temperature = HNRS.requestTemperature()
                .orElseThrow(() -> new NullPointerException("Failed: The request temperature data does not have a value."));

        hangangNowData.update(temperature);
        return temperature;
    }

    @Scheduled(cron = "0 0 0/3 * * *", zone = "Asia/Seoul")
    public Dust updateDust() throws IOException, ParseException {
        HangangNowData hangangNowData = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        Dust dust = HNRS.requestDust()
                .orElseThrow(() -> new NullPointerException("Failed: The request dust data does not have a value."));

        hangangNowData.update(dust);
        return dust;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    public SunMoonRiseSet updateSunMoonRiseSet() throws IOException, JDOMException {
        LocalDate currentDate = LocalDate.now();
        HangangNowData hangangNowData
                = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        SunMoonRiseSet sunMoonRiseSet = HNRS.requestSunMoonRiseSet();
        hangangNowData.update(sunMoonRiseSet);
        return sunMoonRiseSet;
    }
}
