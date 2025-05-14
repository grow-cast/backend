package com.growcast.growcast.grownCrops.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class GrownCropsResponse {
    private Long gcId;
    private String name;
    private String nickname;
    private String picture;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
}
