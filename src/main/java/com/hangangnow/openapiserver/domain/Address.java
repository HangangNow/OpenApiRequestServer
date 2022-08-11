package com.hangangnow.openapiserver.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
public class Address {

    private String sido;
    private String gu;
    private String detail;


    public Address(String sido, String gu, String detail) {
        this.sido = sido;
        this.gu = gu;
        this.detail = detail;
    }
}