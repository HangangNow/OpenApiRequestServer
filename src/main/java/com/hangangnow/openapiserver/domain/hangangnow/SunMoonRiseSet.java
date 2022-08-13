package com.hangangnow.openapiserver.domain.hangangnow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SunMoonRiseSet {
    private String SunRise;
    private String SunSet;
    private String moonRise;
}
