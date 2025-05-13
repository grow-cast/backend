package com.growcast.growcast.grownCrops.controller;

import com.growcast.growcast.grownCrops.dto.GrownCropsDropdownDTO;
import com.growcast.growcast.grownCrops.service.GrownCropsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crops")
@RequiredArgsConstructor
public class GrownCropsController {
    private final GrownCropsService grownCropsService;

    //위 아래 출력 값이 다름 프론트쪽에 물어보고 구현한 거에 맞춰서 사용하기
    @GetMapping("/dashboard/grownCrops")
    public ResponseEntity<List<GrownCropsDropdownDTO>> getGrownCrops() {
        Long user_id = 1L; // 구글 소셜 로그인 구현 후 JWT 사용해 userId 추출해서 사용하는 걸로 변경 예정

        List<GrownCropsDropdownDTO> grownCropsDropdownDTO = grownCropsService.getGrownCropsDropdown(user_id);

        return ResponseEntity.ok(grownCropsDropdownDTO);
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
