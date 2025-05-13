package com.growcast.growcast.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthResponse {
    private Long userId;
    private String nickname;
    private String accessToken;
    private String refreshToken;
    private String googleId;  //signup_required일 때만 사용
    private String status;    //로그인 아니면 signup_required
}
