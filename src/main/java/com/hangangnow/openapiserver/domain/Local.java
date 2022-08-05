package com.hangangnow.openapiserver.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Local {

    private String localName;
    private Double x_pos;
    private Double y_pos;

    public Local(){
    }

    public Local(String localName, Double x_pos, Double y_pos) {
        this.localName = localName;
        this.x_pos = x_pos;
        this.y_pos = y_pos;
    }
}