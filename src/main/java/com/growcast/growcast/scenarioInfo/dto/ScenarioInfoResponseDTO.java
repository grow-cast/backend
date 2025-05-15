package com.growcast.growcast.scenarioInfo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor  //기본 생성자 (필수)
@AllArgsConstructor //모든 필드를 받는 생성자 자동 생성
public class ScenarioInfoResponseDTO {
    private String summary; //시나리오 요약
    private String recommendationNote; //추천 방향성
}