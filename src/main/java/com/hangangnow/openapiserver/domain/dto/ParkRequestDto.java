package com.hangangnow.openapiserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkRequestDto {

    private Long parkId;
    private String parkName;
    private String si;
    private String gu;
    private String gil;
    private String detail;
    private Double x_pos;
    private Double y_pos;
    private String content;
    private LocalDateTime lastModifiedTime;

}
