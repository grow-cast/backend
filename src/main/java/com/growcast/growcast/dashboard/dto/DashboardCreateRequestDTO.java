package com.growcast.growcast.dashboard.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor  //기본 생성자 (필수)
@AllArgsConstructor //모든 필드를 받는 생성자 자동 생성
public class DashboardCreateRequestDTO {
    private String dashboardTitle; //대시보드 제목
    private String picture; //이미지 경로(gcs 사용 예정)
    private String cropName; //작물 이름
    private Integer waterTime; //물 준 시간
    private Integer waterAmount; //물 양
    private String nutritionalSupplements; //영양제
    private Integer nutritionalSupplementsTime; //영양제 준 시간
    private Integer nutritionalSupplementsAmount; //영양제 양
    private String usedPesticide; //사용한 농약
    private String cropGrowthStatus; //작물 생장 상태
    private Integer number; //밭에 나간 횟수
    private String workHistory; //작업 내용
    private LocalDate harvesMonth; //수확일
    private Integer harvesAmount; //수확량
    private String harvesState; //수확 상태
    private String storageMethod; //저장 방법
    private Long gcId; //작물 아이디 -> 드롭다운에서 선택했을 경우에만 저장
}
