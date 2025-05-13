package com.growcast.growcast.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String googleId;
    private String nickname;
    private String region;
}