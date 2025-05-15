package com.growcast.growcast.cropsRecommend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CropsRecommendResponse {
    private List<RecommendedCropDTO> recommended;
}
