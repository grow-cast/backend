package com.growcast.growcast.cropsRecommend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CropsRecommendRequest {
    private String regionCode;
    private int year;
}
