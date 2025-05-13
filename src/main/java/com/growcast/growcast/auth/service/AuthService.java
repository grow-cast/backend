package com.growcast.growcast.auth.service;

import com.growcast.growcast.auth.dto.AuthResponse;
import com.growcast.growcast.auth.dto.SignupRequest;
import com.growcast.growcast.config.JwtUtil;
import com.growcast.growcast.user.entity.User;
import com.growcast.growcast.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final GoogleTokenVerifier googleTokenVerifier;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //Google 로그인 처리
    public ResponseEntity<?> authenticateWithGoogle(String idToken) {
        String googleId = googleTokenVerifier.verify(idToken);
        if (googleId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "유효하지 않은 Google ID 토큰입니다."));
        }

        Optional<User> optionalUser = userRepository.findByGoogleId(googleId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String accessToken = jwtUtil.generateToken(user.getUserId(), user.getNickname());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

            return ResponseEntity.ok(AuthResponse.builder()
                    .userId(user.getUserId())
                    .nickname(user.getNickname())
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .status("login")
                    .build());
        }

        return ResponseEntity.status(201).body(AuthResponse.builder()
                .googleId(googleId)
                .status("signup_required")
                .build());
    }

    //회원가입 처리
    public ResponseEntity<?> signup(SignupRequest request) {
        if (userRepository.findByGoogleId(request.getGoogleId()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "이미 등록된 사용자입니다."));
        }

        User user = User.builder()
                .googleId(request.getGoogleId())
                .nickname(request.getNickname())
                .regionCode(request.getRegion())
                .createdAt(LocalDateTime.now())
                .build();

        userRepository.save(user);

        String accessToken = jwtUtil.generateToken(user.getUserId(), user.getNickname());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserId());

        return ResponseEntity.ok(AuthResponse.builder()
                .userId(user.getUserId())
                .nickname(user.getNickname())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .status("login")
                .build());
    }

    //액세스 토큰 재발급
    public String refreshAccessToken(String refreshToken) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(refreshToken);
            if (!jwtUtil.validateToken(refreshToken)) return null;
            if (!jwtUtil.isRefreshTokenMatch(userId, refreshToken)) return null;

            Optional<User> optionalUser = userRepository.findById(userId);
            if (optionalUser.isEmpty()) return null;

            User user = optionalUser.get();
            return jwtUtil.generateToken(user.getUserId(), user.getNickname());
        } catch (Exception e) {
            return null;
        }
    }

    //로그아웃
    public ResponseEntity<?> logout(String accessToken) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(accessToken);
            jwtUtil.revokeRefreshToken(userId);
            return ResponseEntity.ok(Map.of("message", "로그아웃 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }
}
