package com.hangangnow.openapiserver.domain.hangangnow;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Dust {
    private int dust10Grade;
    private int dust25Grade;
    private int badderDustGrade;
}
