package com.growcast.growcast.dashboard.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor  //기본 생성자 (필수)
@AllArgsConstructor //모든 필드를 받는 생성자 자동 생성
public class DashboardMonthlyListDTO {
    private Long dashboardId;
    private String dashboardTitle; //대시보드 제목
    private LocalDate date;
}