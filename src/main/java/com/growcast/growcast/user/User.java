package com.growcast.growcast.user;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
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
    private Long user_id;

    @Column(name = "google_id", nullable = false)
    private Long google_id;

    @Column(name = "nickname", nullable = false, length = 225)
    private String nickname;

    @Column(name = "regionCode", nullable = false)
    private Integer regionCode;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    /* 위 로컬데이터타입으로 생성할 때 오류가 발생하면 이 코드 사용하면 됨
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    */
}
