package com.hangangnow.openapiserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingRequestDto {

    private String contentId;
    private String parkName;

    private String si;
    private String gu;
    private String detail;

    private String parkingName;
    private Integer availableCount;
    private Integer totalCount;


}
