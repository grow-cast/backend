package com.growcast.growcast.user.controller;

import com.growcast.growcast.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //회원 정보 조회
    @GetMapping("/me")
    public ResponseEntity<?> getProfile(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);
        return userService.getProfile(accessToken);
    }

    //회원 정보 수정(지역 변경)
    @PatchMapping("/me")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,
                                           @RequestBody Map<String, String> requestBody) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);
        String newRegion = requestBody.get("regionCode");

        if (newRegion == null || newRegion.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "regionCode를 입력해주세요."));
        }

        return userService.updateProfile(accessToken, newRegion);
    }

    //회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);
        return userService.deleteUser(accessToken);
    }
}
