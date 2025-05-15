package com.growcast.growcast.dashboard.service;

import com.growcast.growcast.dashboard.dto.DashboardCreateRequestDTO;
import com.growcast.growcast.dashboard.dto.DashboardMonthlyListDTO;
import com.growcast.growcast.dashboard.dto.DashboardDetailDTO;
import com.growcast.growcast.dashboard.dto.DashboardUpdateDTO;
import com.growcast.growcast.dashboard.entity.Dashboard;
import com.growcast.growcast.dashboard.repository.DashboardRepository;
import com.growcast.growcast.grownCrops.entity.GrownCrops;
import com.growcast.growcast.grownCrops.repository.GrownCropsRepository;
import com.growcast.growcast.grownCrops.service.GrownCropsService;
import com.growcast.growcast.user.entity.User;
import com.growcast.growcast.user.repository.UserRepository;
import com.growcast.growcast.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;  // MultipartFile import
import java.io.IOException;  // IOException import
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Autowired;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final UserRepository userRepository;
    private final GrownCropsRepository grownCropsRepository;
    private final GrownCropsService grownCropsService;
    private final JwtUtil jwtUtil;

    @Autowired
    private final Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    // GCS에 이미지 업로드
    public String uploadImageToGCS(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("파일이 비어있습니다.");
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, "dashboard/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        return "https://storage.googleapis.com/" + bucketName + "/dashboard/" + fileName;
    }

    //대시보드 등록
    public void createDashboard(String accessToken, DashboardCreateRequestDTO dashboardCreateRequestDTO) throws IOException  {
        Long user_id = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        GrownCrops grownCrops = null;

        //작물 이름이 사전에 등록되어 있었는지 여부 검사
        String cropName = dashboardCreateRequestDTO.getCropName();
        Optional<Long> grownCropsIdOptional = grownCropsRepository.findGcIdByUserIdAndCropName(user_id, cropName);

        if (grownCropsIdOptional.isPresent()) {
            Long gcId = (Long) grownCropsIdOptional.get();
            grownCrops = grownCropsRepository.findById(gcId).orElse(null);
        }


        // 이미지 업로드 후 URL 받기
        String imageUrl = null;
        if (dashboardCreateRequestDTO.getPicture() != null) {
            imageUrl = uploadImageToGCS(dashboardCreateRequestDTO.getPicture());
        }

        //대시보드 저장
        Dashboard dashboard = Dashboard.builder()
                .dashboardTitle(dashboardCreateRequestDTO.getDashboardTitle())
                .picture(imageUrl)
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
                .createdAt(LocalDateTime.now())
                .build();

        dashboardRepository.save(dashboard);
    }

    //대시보드 상세보기
    public DashboardDetailDTO getDashboardDetail(String accessToken, Long dashboardId) {
        Long user_id = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new IllegalArgumentException("대시보드가 존재하지 않습니다"));

        return new DashboardDetailDTO (
                dashboard.getDashboardTitle(),
                dashboard.getPicture(),
                dashboard.getCropName(),
                dashboard.getWaterTime(),
                dashboard.getWaterAmount(),
                dashboard.getNutritionalSupplements(),
                dashboard.getNutritionalSupplementsTime(),
                dashboard.getNutritionalSupplementsAmount(),
                dashboard.getUsedPesticide(),
                dashboard.getCropGrowthStatus(),
                dashboard.getNumber(),
                dashboard.getWorkHistory(),
                dashboard.getHarvesMonth(),
                dashboard.getHarvesAmount(),
                dashboard.getHarvesState(),
                dashboard.getStorageMethod()
        );
    }

    //대시보드 삭제
    public void deleteDashboard(String accessToken, Long dashboardId) {
        Long user_id = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new IllegalArgumentException("대시보드가 존재하지 않습니다"));

        dashboardRepository.deleteById(dashboardId);
    }

    //대시보드 수정
    public void updateDashboard(String accessToken, Long dashboardId, DashboardUpdateDTO dashboardUpdateDTO) throws IOException {
        Long user_id = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        Dashboard dashboard = dashboardRepository.findById(dashboardId)
                .orElseThrow(() -> new IllegalArgumentException("대시보드가 존재하지 않습니다"));


        //이미지 경로 업데이트
        String imageUrl = dashboard.getPicture();

        if (dashboardUpdateDTO.getPicture() != null) {
            imageUrl = uploadImageToGCS(dashboardUpdateDTO.getPicture());
        }

        //수정사항 저장
        if (dashboardUpdateDTO.getDashboardTitle() != null) dashboard.setDashboardTitle(dashboardUpdateDTO.getDashboardTitle());
        if (dashboardUpdateDTO.getPicture() != null) dashboard.setPicture(imageUrl);
        if (dashboardUpdateDTO.getCropName() != null) dashboard.setCropName(dashboardUpdateDTO.getCropName());
        if (dashboardUpdateDTO.getWaterTime() != null) dashboard.setWaterTime(dashboardUpdateDTO.getWaterTime());
        if (dashboardUpdateDTO.getWaterAmount() != null) dashboard.setWaterAmount(dashboardUpdateDTO.getWaterAmount());
        if (dashboardUpdateDTO.getNutritionalSupplements() != null) dashboard.setNutritionalSupplements(dashboardUpdateDTO.getNutritionalSupplements());
        if (dashboardUpdateDTO.getNutritionalSupplementsTime() != null) dashboard.setNutritionalSupplementsTime(dashboardUpdateDTO.getNutritionalSupplementsTime());
        if (dashboardUpdateDTO.getNutritionalSupplementsAmount() != null) dashboard.setNutritionalSupplementsAmount(dashboardUpdateDTO.getNutritionalSupplementsAmount());
        if (dashboardUpdateDTO.getUsedPesticide() != null) dashboard.setUsedPesticide(dashboardUpdateDTO.getUsedPesticide());
        if (dashboardUpdateDTO.getCropGrowthStatus() != null) dashboard.setCropGrowthStatus(dashboardUpdateDTO.getCropGrowthStatus());
        if (dashboardUpdateDTO.getNumber() != null) dashboard.setNumber(dashboardUpdateDTO.getNumber());
        if (dashboardUpdateDTO.getWorkHistory() != null) dashboard.setWorkHistory(dashboardUpdateDTO.getWorkHistory());
        if (dashboardUpdateDTO.getHarvestMonth() != null) dashboard.setHarvesMonth(dashboardUpdateDTO.getHarvestMonth());
        if (dashboardUpdateDTO.getHarvestAmount() != null) dashboard.setHarvesAmount(dashboardUpdateDTO.getHarvestAmount());
        if (dashboardUpdateDTO.getHarvestState() != null) dashboard.setHarvesState(dashboardUpdateDTO.getHarvestState());
        if (dashboardUpdateDTO.getStorageMethod() != null) dashboard.setStorageMethod(dashboardUpdateDTO.getStorageMethod());

        dashboardRepository.save(dashboard);
    }

    //달력 ui에 대시보드 목록 가져옴
    public List<DashboardMonthlyListDTO> getMonthlyDashboards(String accessToken, YearMonth yearMonth) {
        Long user_id = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

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

        return dashboardRepository.findAllByUser_UserIdAndCreatedAtBetween(user_id, startTime, endTime)
                .stream()
                .map(dashboard -> new DashboardMonthlyListDTO(
                        dashboard.getDashboardId(),
                        dashboard.getDashboardTitle(),
                        dashboard.getCreatedAt().toLocalDate()
                ))
                .collect(Collectors.toList());
    }
}