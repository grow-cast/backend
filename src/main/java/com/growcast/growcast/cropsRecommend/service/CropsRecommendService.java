package com.growcast.growcast.cropsRecommend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendRequest;
import com.growcast.growcast.cropsRecommend.dto.CropsRecommendResponse;
import com.growcast.growcast.cropsRecommend.dto.RecommendedCropDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CropsRecommendService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String AI_SERVER_URL = "http://34.47.71.239:8000/crop_recommendation";

    public CropsRecommendResponse getRecommendations(CropsRecommendRequest request) {
        try {
            //쿼리 파라미터 url 만들기
            String urlWithParams = UriComponentsBuilder.fromHttpUrl(AI_SERVER_URL)
                    .queryParam("region", request.getRegionCode())
                    .queryParam("year", request.getYear())
                    .toUriString();

            ResponseEntity<String> response = restTemplate.getForEntity(urlWithParams, String.class);

            JsonNode body = objectMapper.readTree(response.getBody());
            JsonNode recommendedList = body.get("recommended_crops");

            List<RecommendedCropDTO> result = new ArrayList<>();
            for (JsonNode cropNode : recommendedList) {
                String rawCrop = cropNode.get("crop").asText();
                String rawReason = cropNode.get("reason").asText();

                //작물명 정제 - 제미나이 ** 뜯기
                String cleanCrop = rawCrop.replace("**", "").trim();

                //이유 정제 - 줄바꿈표시 뜯기..., 참고도
                String cleanReason = rawReason.replace("**", "")
                        .replace("\n", " ")
                        .replaceAll("\\s{2,}", " ")
                        .trim();

                int referenceIndex = cleanReason.indexOf("참고:");
                if (referenceIndex != -1) {
                    cleanReason = cleanReason.substring(0, referenceIndex).trim();
                }

                result.add(new RecommendedCropDTO(cleanCrop, cleanReason));
            }

            return new CropsRecommendResponse(result);
        } catch (Exception e) {
            throw new RuntimeException("AI 서버 응답 실패", e);
        }
    }
}
