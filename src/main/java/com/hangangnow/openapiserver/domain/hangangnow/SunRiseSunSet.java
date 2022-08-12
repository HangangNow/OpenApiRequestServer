package com.hangangnow.openapiserver.domain.hangangnow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SunRiseSunSet {
    private String todaySunRise;
    private String todaySunSet;
    private String tomorrowSunRise;

    public SunRiseSunSet(String todaySunRise, String todaySunSet){
        this.todaySunRise = todaySunRise;
        this.todaySunSet = todaySunSet;
        this.tomorrowSunRise = "empty";
    }

    public SunRiseSunSet updateTomorrowSunRise(String tomorrowSunRise){
        this.tomorrowSunRise = tomorrowSunRise;
        return this;
    }
}
