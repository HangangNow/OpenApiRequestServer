package com.hangangnow.openapiserver.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkPhotoRequestDto {
    private String contentId;
    private HashMap<String, String> hashMap = new HashMap<>();
}
