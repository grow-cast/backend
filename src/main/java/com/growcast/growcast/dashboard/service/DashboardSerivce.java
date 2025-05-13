package com.growcast.growcast.dashboard.service;

import com.growcast.growcast.dashboard.dto.DashboardCreateRequestDTO;
import com.growcast.growcast.dashboard.dto.DashboardMonthlyListDTO;
import com.growcast.growcast.dashboard.entity.Dashboard;
import com.growcast.growcast.dashboard.repository.DashboardRepository;
import com.growcast.growcast.grownCrops.entity.GrownCrops;
import com.growcast.growcast.grownCrops.repository.GrownCropsRepository;
import com.growcast.growcast.grownCrops.service.GrownCropsService;
import com.growcast.growcast.user.entity.User;
import com.growcast.growcast.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardSerivce {
    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;
    private final GrownCropsRepository grownCropsRepository;
    private final GrownCropsService grownCropsService;

    //대시보드 등록
    public void createDashboard(DashboardCreateRequestDTO dashboardCreateRequestDTO, long user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));
        GrownCrops grownCrops = null;

        //작물 이름이 사전에 등록되어 있었는지 여부 검사
        String cropName = dashboardCreateRequestDTO.getCropName();
        Optional<Long> grownCropsIdOptional = grownCropsRepository.findByGrownCropsNameAndUserId(user_id, cropName);

        if (grownCropsIdOptional.isPresent()) {
            Long gcId = (Long) grownCropsIdOptional.get();
            grownCrops = grownCropsRepository.findById(gcId).orElse(null);
        }

        //대시보드 저장
        Dashboard dashboard = Dashboard.builder()
                .dashboardTitle(dashboardCreateRequestDTO.getDashboardTitle())
                .picture(dashboardCreateRequestDTO.getPicture())
                .cropName(cropName)
                .waterTime(dashboardCreateRequestDTO.getWaterTime())
                .waterAmount(dashboardCreateRequestDTO.getWaterAmount())
                .nutritionalSupplements(dashboardCreateRequestDTO.getNutritionalSupplements())
                .nutritionalSupplementsAmount(dashboardCreateRequestDTO.getNutritionalSupplementsAmount())
                .nutritionalSupplementsTime(dashboardCreateRequestDTO.getNutritionalSupplementsTime())
                .usedPesticide(dashboardCreateRequestDTO.getUsedPesticide())
                .cropGrowthStatus(dashboardCreateRequestDTO.getCropGrowthStatus())
                .number(dashboardCreateRequestDTO.getNumber())
                .workHistory(dashboardCreateRequestDTO.getWorkHistory())
                .harvesMonth(dashboardCreateRequestDTO.getHarvesMonth())
                .harvesAmount(dashboardCreateRequestDTO.getHarvesAmount())
                .harvesState(dashboardCreateRequestDTO.getHarvesState())
                .storageMethod(dashboardCreateRequestDTO.getStorageMethod())
                .user(user)
                .grownCrops(grownCrops)
                .build();

        dashboardRepository.save(dashboard);
    }
    //대시보드 수정

    //대시보드 삭제
    public void deleteDashboard(long user_id) {
        dashboardRepository.deleteById(user_id);
    }


    //달력 ui에 대시보드 목록 가져옴
    public List<DashboardMonthlyListDTO> getMonthlyDashboards(Long user_id, YearMonth yearMonth) {
        //월의 시작일과 종료일 구하기
        LocalDate start = yearMonth.atDay(1);
        LocalDate end = yearMonth.atEndOfMonth();

        /* db에 한국시간으로 저장되지 않을 경우 이거 사
        ZoneId koreaZone = ZoneId.of("Asia/Seoul");
        ZoneId utcZone = ZoneId.of("UTC");

        //시작 시간 (00:00:00) - 종료시간 (23:59:58)
        LocalDateTime startTime = start.atStartOfDay(용).atZone(koreaZone).withZoneSameInstant(utcZone).toLocalDateTime();
        */

        //db에 한국 시간으로 저장될 경우(시작 시간 (00:00:00) - 종료시간 (23:59:58))
        LocalDateTime startTime = start.atStartOfDay();
        LocalDateTime endTime = end.atTime(23, 59, 59);

        return dashboardRepository.findAllDashboardByUserIdAndCreatedAtBetween(user_id, startTime, endTime)
                .stream()
                .map(dashboard -> new DashboardMonthlyListDTO(
                        dashboard.getDashboard_id(),
                        dashboard.getDashboardTitle(),
                        dashboard.getCreatedAt().toLocalDate()
                ))
                .collect(Collectors.toList());

    }
}
