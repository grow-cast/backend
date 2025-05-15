package com.growcast.growcast.cropsRecommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecommendedCropDTO {
    private String recommendedCropName;
    private String reason;
}
