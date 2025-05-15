package com.growcast.growcast.grownCrops.controller;

import com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO;
import com.growcast.growcast.grownCrops.dto.GrownCropsRequest;
import com.growcast.growcast.grownCrops.dto.GrownCropsResponse;
import com.growcast.growcast.grownCrops.service.GrownCropsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/crops")
@RequiredArgsConstructor
public class GrownCropsController {
    private final GrownCropsService grownCropsService;

    //위 아래 출력 값이 다름 프론트쪽에 물어보고 구현한 거에 맞춰서 사용하기
    @GetMapping("/dashboard/grownCrops")
    public ResponseEntity<?> getGrownCrops(@RequestHeader("Authorization") String token) {
        //Long user_id = 1L; // 구글 소셜 로그인 구현 후 JWT 사용해 userId 추출해서 사용하는 걸로 변경 예정

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);

        List<GrownCropsDropdownDTO> grownCropsDropdownDTO = grownCropsService.getGrownCropsDropdown(accessToken);

        return ResponseEntity.ok(grownCropsDropdownDTO);
    }

    @PostMapping(value = "/registration", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerCropWithImage(
            @RequestHeader("Authorization") String token,
            @RequestPart("data") GrownCropsRequest request,
            @RequestPart("picture") MultipartFile pictureFile
    ) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String accessToken = token.substring(7);

        try {
            Long gcId = grownCropsService.registerCropWithImage(request, accessToken, pictureFile);
            return ResponseEntity.ok(Map.of("gcId", gcId, "message", "등록 완료"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "이미지 업로드 실패"));
        }
    }


    //목록가져오기
    @GetMapping("")
    public ResponseEntity<?> getAllCrops(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String accessToken = token.substring(7);

        List<GrownCropsResponse> crops = grownCropsService.getAllCrops(accessToken);
        return ResponseEntity.ok(crops);
    }

    //수정
    @PutMapping("/{gcid}")
    public ResponseEntity<?> updateCrop(@PathVariable("gcid") Long gcId,
                                        @RequestHeader("Authorization") String token,
                                        @RequestBody GrownCropsRequest request) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String accessToken = token.substring(7);

        try {
            grownCropsService.updateCrop(gcId, request, accessToken);
            return ResponseEntity.ok(Map.of("message", "수정 완료"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    //삭제
    @DeleteMapping("/{gcid}")
    public ResponseEntity<?> deleteCrop(@PathVariable("gcid") Long gcId,
                                        @RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        String accessToken = token.substring(7);

        try {
            grownCropsService.deleteCrop(gcId, accessToken);
            return ResponseEntity.ok(Map.of("message", "삭제 완료"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    /*public ResponseEntity<Map<String, Object>> getGrownCropsDropdown() {
        Long user_id = 1L; //구글 소셜 로그인 구현 후 JWT 사용해 userId 추출해서 사용하는 걸로 변경 예정

        List<GrownCropsDropdownDTO> grownCropsDropdownDTO = grownCropsService.getGrownCropsDropdown(user_id);

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 201);
        response.put("message", "드롭다운용 작물 이름 가져오기 성공");
        response.put("grownCropsName", grownCropsDropdownDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }*/
}