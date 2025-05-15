package com.growcast.growcast.cropsRecommend.controller;

import com.growcast.growcast.config.JwtUtil;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendRequest;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendResponse;
import com.growcast.growcast.cropsRecommend.service.CropsRecommendService;
import com.growcast.growcast.scenarioInfo.service.ScenarioInfoService;
import com.growcast.growcast.scenarioInfo.dto.ScenarioInfoResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cropsRecommend")
@RequiredArgsConstructor
public class CropsRecommendController {
    private final ScenarioInfoService scenarioInfoService;
    private final CropsRecommendService cropsRecommendService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> recommend(@RequestHeader("Authorization") String token,
                                       @RequestBody CropsRecommendRequest request) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);

        try {
            //시나리오 추천
            ScenarioInfoResponseDTO scenarioInfoResponseDTO = scenarioInfoService.getAndSaveScenarioInfo(accessToken, request);

            //작물 추천
            CropsRecommendResponse response = cropsRecommendService.getRecommendations(request);
            
            //통합 응답 반환
            return ResponseEntity.ok(Map.of(
                "scenario", scenarioInfoResponseDTO,
                "cropsRecommendation", response
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "응답없음"));
        }
    }
}
