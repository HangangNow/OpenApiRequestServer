package com.hangangnow.openapiserver.repository;

import com.hangangnow.openapiserver.domain.hangangnow.Weather;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class WeatherRepository {
    private final EntityManager em;

    void save(Weather weather){
        em.persist(weather);
    }
}
