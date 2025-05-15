package com.growcast.growcast.scenarioInfo.service;

import com.growcast.growcast.scenarioInfo.dto.ScenarioInfoResponseDTO;
import org.springframework.web.client.RestTemplate;
import com.growcast.growcast.user.repository.UserRepository;
import com.growcast.growcast.user.entity.User;
import com.growcast.growcast.climate.entity.Climate;
import com.growcast.growcast.scenarioInfo.entity.ScenarioInfo;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.growcast.growcast.config.JwtUtil;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class ScenarioInfoService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RestTemplate restTemplate;

    public ScenarioInfoResponseDTO getAndSaveScenarioInfo(String accessToken, CropsRecommendRequest request) {
        Long userId = jwtUtil.getUserIdFromToken(accessToken);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));

        String region = request.getRegionCode();
        int year = request.getYear();

        //AI API 호출 URL 생성
        String url = String.format("http://34.47.71.239:8000/climate_scenario?region=%s&year=%d", region, year);

        // AI API GET 호출
        ResponseEntity<ScenarioInfoResponseDTO> response = restTemplate.getForEntity(url, ScenarioInfoResponseDTO.class);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new RuntimeException("AI API 호출 실패: " + response.getStatusCode());
        }

        ScenarioInfoResponseDTO scenarioInfoResponseDTO = response.getBody();

        return scenarioInfoResponseDTO;
    }
}