package com.growcast.growcast.auth.controller;

import com.growcast.growcast.auth.dto.GoogleAuthRequest;
import com.growcast.growcast.auth.dto.SignupRequest;
import com.growcast.growcast.auth.dto.TokenRefreshRequest;
import com.growcast.growcast.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/google")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleAuthRequest request) {
        return authService.authenticateWithGoogle(request.getIdToken());
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request) {
        if (request.getGoogleId() == null || request.getNickname() == null || request.getRegion() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "필수 정보를 모두 입력해야 합니다."));
        }
        return authService.signup(request);
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest request) {
        String newAccessToken = authService.refreshAccessToken(request.getRefreshToken());
        if (newAccessToken == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid or expired refresh token"));
        }
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        if (token == null || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
        String accessToken = token.substring(7);
        return authService.logout(accessToken);
    }
}
