package com.hangangnow.openapiserver.service;

import com.hangangnow.openapiserver.domain.hangangnow.Dust;
import com.hangangnow.openapiserver.domain.hangangnow.HangangNowData;
import com.hangangnow.openapiserver.domain.hangangnow.SunRiseSunSet;
import com.hangangnow.openapiserver.domain.hangangnow.Weather;
import com.hangangnow.openapiserver.repository.HangangNowRepository;
import lombok.RequiredArgsConstructor;
import org.jdom2.JDOMException;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDate currentDate = currentDateTime.toLocalDate();

        Double temperature = HNRS.requestTemperature()
                .orElseThrow(() -> new NullPointerException("Failed: The request temperature data does not have a value."));
        Dust dust = HNRS.requestDust()
                .orElseThrow(() -> new NullPointerException("Failed: The request dust data does not have a value."));
        Weather weather = HNRS.requestWeather();
        SunRiseSunSet sunRiseSunSet = HNRS.requestSunRiseSunSet(currentDate);

        HangangNowData initHangangNowData = new HangangNowData(
                1L,
                currentDateTime,
                temperature,
                weather,
                dust,
                sunRiseSunSet
        );
        hangangNowRepository.save(initHangangNowData);
        return initHangangNowData;
    }

    public Weather updateWeather() throws IOException, ParseException {
        HangangNowData hangangNowData = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        Weather weather = HNRS.requestWeather();
        hangangNowData.update(weather);
        return weather;
    }

    public Double updateTemperature() throws IOException, ParseException {
        HangangNowData hangangNowData = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        Double temperature = HNRS.requestTemperature()
                .orElseThrow(() -> new NullPointerException("Failed: The request temperature data does not have a value."));

        hangangNowData.update(temperature);
        return temperature;
    }

    public Dust updateDust() throws IOException, ParseException {
        HangangNowData hangangNowData = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        Dust dust = HNRS.requestDust()
                .orElseThrow(() -> new NullPointerException("Failed: The request dust data does not have a value."));

        hangangNowData.update(dust);
        return dust;
    }

    public SunRiseSunSet updateSunRiseSunSet(LocalDate localDate) throws IOException, JDOMException {
        HangangNowData hangangNowData
                = hangangNowRepository.findHangangNowData()
                .orElseThrow(() -> new NullPointerException("Failed: Not found hangangNow Data"));
        SunRiseSunSet sunRiseSunSet = HNRS.requestSunRiseSunSet(localDate);
        hangangNowData.update(sunRiseSunSet);
        return sunRiseSunSet;
    }
}
