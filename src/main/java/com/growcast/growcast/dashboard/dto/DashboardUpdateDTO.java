package com.growcast.growcast.dashboard.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class DashboardUpdateDTO {
    private String dashboardTitle; // 대시보드 제목
    private MultipartFile picture; // 이미지 경로 (수정 시에도 이미지 경로를 전달할 수 있도록 함)
    private String cropName; // 작물 이름
    private String waterTime; // 물 준 시간
    private Integer waterAmount; // 물 양
    private String nutritionalSupplements; // 영양제
    private String nutritionalSupplementsTime; // 영양제 준 시간
    private Integer nutritionalSupplementsAmount; // 영양제 양
    private String usedPesticide; // 사용한 농약
    private String cropGrowthStatus; // 작물 생장 상태
    private Integer number; // 밭에 나간 횟수
    private String workHistory; // 작업 내용
    private LocalDate harvestMonth; // 수확일
    private Integer harvestAmount; // 수확량
    private String harvestState; // 수확 상태
    private String storageMethod; // 저장 방법
}
