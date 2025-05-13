package com.growcast.growcast.user.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;  //필드명 변경

    @Column(name = "google_id", nullable = false)
    private String googleId;  //필드명 변경

    @Column(name = "nickname", nullable = false, length = 225)
    private String nickname;

    @Column(name = "regionCode", columnDefinition = "TEXT", nullable = false)
    private String regionCode;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;
}
