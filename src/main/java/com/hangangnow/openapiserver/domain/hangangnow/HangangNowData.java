package com.hangangnow.openapiserver.domain.hangangnow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HangangNowData {
    @Id
    private Long id;
    private LocalDateTime lastModifiedTime;
    private Double temperature;

    @Embedded
    private Weather weather;
    @Embedded
    private Dust dust;
    @Embedded
    private SunRiseSunSet sunRiseSunSet;

    public void update(Double temperature){
        this.temperature = temperature;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void update(Weather weather){
        this.weather = weather;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void update(Dust dust){
        this.dust = dust;
        this.lastModifiedTime = LocalDateTime.now();
    }

    public void update(SunRiseSunSet sunRiseSunSet){
        this.sunRiseSunSet = sunRiseSunSet;
        this.lastModifiedTime = LocalDateTime.now();
    }
}
