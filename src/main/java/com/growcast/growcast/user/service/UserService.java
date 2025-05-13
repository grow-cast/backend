package com.growcast.growcast.user.service;

import com.growcast.growcast.user.entity.User;
import com.growcast.growcast.user.repository.UserRepository;
import com.growcast.growcast.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    //회원 정보 조회
    public ResponseEntity<?> getProfile(String accessToken) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(accessToken);
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            User user = optionalUser.get();
            return ResponseEntity.ok(Map.of(
                    "userId", user.getUserId(),
                    "nickname", user.getNickname(),
                    "regionCode", user.getRegionCode()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }

    //회원 정보 수정
    public ResponseEntity<?> updateProfile(String accessToken, String newRegion) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(accessToken);
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            User user = optionalUser.get();
            user.setRegionCode(newRegion);
            userRepository.save(user);

            return ResponseEntity.ok(Map.of("message", "프로필이 성공적으로 업데이트되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }

    //회원 탈퇴
    public ResponseEntity<?> deleteUser(String accessToken) {
        try {
            Long userId = jwtUtil.getUserIdFromToken(accessToken);
            Optional<User> optionalUser = userRepository.findById(userId);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(404).body(Map.of("error", "사용자를 찾을 수 없습니다."));
            }

            userRepository.deleteById(userId);
            jwtUtil.revokeRefreshToken(userId);

            return ResponseEntity.ok(Map.of("message", "회원 탈퇴가 완료되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }
}
