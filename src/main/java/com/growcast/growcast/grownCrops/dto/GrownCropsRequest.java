package com.growcast.growcast.grownCrops.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GrownCropsRequest {
    @NotBlank(message = "작물 이름은 필수입니다.")
    private String name;
    private String nickname;
    private String picture;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
}