package com.growcast.growcast.cropsRecommend.controller;

import com.growcast.growcast.config.JwtUtil;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendRequest;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendResponse;
import com.growcast.growcast.cropsRecommend.service.CropsRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cropsRecommend")
@RequiredArgsConstructor
public class CropsRecommendController {

    private final CropsRecommendService cropsRecommendService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> recommend(@RequestHeader("Authorization") String token,
                                       @RequestBody CropsRecommendRequest request) {

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        try {
            CropsRecommendResponse response = cropsRecommendService.getRecommendations(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "응답없음"));
        }
    }
}
