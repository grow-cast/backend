package com.growcast.growcast.dashboard.Controller;

import com.growcast.growcast.dashboard.dto.DashboardMonthlyListDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.growcast.growcast.dashboard.service.DashboardService;
import com.growcast.growcast.dashboard.dto.DashboardCreateRequestDTO;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    //출력 값이 다름 프론트쪽에 물어보고 맞춰서 수정
    //대시보드 작성 api
    @PostMapping
    public ResponseEntity<?> createDashboard(@RequestHeader("Authorization") String token, @RequestBody DashboardCreateRequestDTO dashboardCreateRequestDTO) {
        //Long user_id = 1L; //구글 소셜 로그인 구현 후 JWT 사용해 userId 추출해서 사용하는 걸로 변경 예정

        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);

        dashboardService.createDashboard(accessToken,dashboardCreateRequestDTO);

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 201);
        response.put("message", "대시보드 작성 성공");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //대시보드 삭제 api
    @DeleteMapping("/{dashboardId}")
    public ResponseEntity<?> deleteDashboard(@RequestHeader("Authorization") String token, @PathVariable("dashboardId") Long dashboardId, HttpServletRequest request) {
        //Long user_id = 1L; //구글 소셜 로그인 구현 후 JWT 사용해 userId 추출해서 사용하는 걸로 변경 예정
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);

        dashboardService.deleteDashboard(accessToken, dashboardId);

        // Referer (이전 페이지 URL) 가져오기
        String referer = request.getHeader("Referer");

        Map<String, Object> response = new HashMap<>();

        response.put("statusCode", 201);
        response.put("message", "대시보드 삭제 성공");
        response.put("previousPage", referer);

        //리다이렉트 할 URL 반환 (이전 페이지로 리다이렉트)
        return ResponseEntity.status(HttpStatus.FOUND).header("Location", referer).body(response);
    }

    //달력데 대시보드 리스트 출력
    @GetMapping("/dashboards/monthly")
    public ResponseEntity<?> getDashboardList(@RequestHeader("Authorization") String token, @RequestParam int year, @RequestParam int month) {
        //Long user_id = 1L; //구글 소셜 로그인 구현 후 JWT 사용해 userId 추출해서 사용하는 걸로 변경 예정
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);

        YearMonth yearMonth = YearMonth.of(year, month); //년/월 -> YearMonth 객체로 변환

        List<DashboardMonthlyListDTO> dashboardMonthlyList = dashboardService.getMonthlyDashboards(accessToken, yearMonth);

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", 200);
        response.put("message", "캘린더 대시보드 목록 리스트 반환 성공");
        response.put("data", dashboardMonthlyList);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}