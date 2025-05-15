package com.growcast.growcast.grownCrops.service;

import com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO;
import com.growcast.growcast.grownCrops.dto.GrownCropsRequest;
import com.growcast.growcast.grownCrops.dto.GrownCropsResponse;
import com.growcast.growcast.grownCrops.entity.GrownCrops;
import com.growcast.growcast.grownCrops.repository.GrownCropsRepository;
import com.growcast.growcast.config.JwtUtil;
import com.growcast.growcast.user.entity.User;
import com.growcast.growcast.user.repository.UserRepository;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GrownCropsService {

    private final GrownCropsRepository grownCropsRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Autowired
    private Storage storage;

    @Value("${gcs.bucket.name}")
    private String bucketName;

    //gcs업로드
    public String uploadToGCS(MultipartFile file) throws IOException {
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        BlobId blobId = BlobId.of(bucketName, "grown-crops/" + fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());

        return "https://storage.googleapis.com/" + bucketName + "/grown-crops/" + fileName;
    }

    //작물 등록 이미지랑
    public Long registerCropWithImage(GrownCropsRequest request, String accessToken, MultipartFile pictureFile) throws IOException {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("작물 이름이 누락되었습니다.");
        }

        String imageUrl = uploadToGCS(pictureFile);  //gcs 업로드

        GrownCrops crop = GrownCrops.builder()
                .name(request.getName())
                .nickname(request.getNickname())
                .picture(imageUrl)
                .plantingDate(request.getPlantingDate())
                .expectedHarvestDate(request.getExpectedHarvestDate())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();

        return grownCropsRepository.save(crop).getGcid();
    }

    //드롭다운용 목록
    public List<GrownCropsDropdownDTO> getGrownCropsDropdown(String accessToken) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        return grownCropsRepository.findByUserId(userId);
    }

    //목록 조회
    public List<GrownCropsResponse> getAllCrops(String accessToken) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);
        return grownCropsRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    //작물 수정
    public void updateCrop(Long gcId, GrownCropsRequest request, String accessToken) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        GrownCrops crop = grownCropsRepository.findByGcidAndUserUserId(gcId, userId)
                .orElseThrow(() -> new IllegalArgumentException("작물을 찾을 수 없습니다."));

        if (request.getName() != null) crop.setName(request.getName());
        if (request.getNickname() != null) crop.setNickname(request.getNickname());
        if (request.getPlantingDate() != null) crop.setPlantingDate(request.getPlantingDate());
        if (request.getExpectedHarvestDate() != null) crop.setExpectedHarvestDate(request.getExpectedHarvestDate());

        grownCropsRepository.save(crop);
    }

    //작물 삭제
    public void deleteCrop(Long gcId, String accessToken) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        GrownCrops crop = grownCropsRepository.findByGcidAndUserUserId(gcId, userId)
                .orElseThrow(() -> new IllegalArgumentException("작물을 찾을 수 없습니다."));

        grownCropsRepository.delete(crop);
    }
}