package com.growcast.growcast.climate;

import com.growcast.growcast.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "climate")
@AllArgsConstructor
@NoArgsConstructor
public class Climate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "climate_id", nullable = false)
    private Long climate_id;

    @Column(name = "regionCode", nullable = false)
    private Integer regionCode;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "temperature", nullable = false)
    private Integer temperature;

    @Column(name = "humidity", nullable = false)
    private Integer humidity;

    @Column(name = "rainfall", nullable = false)
    private Integer rainfall;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "createdAt", nullable = false)
    private LocalDateTime createdAt;

    /* 위 로컬데이터타입으로 생성할 때 오류가 발생하면 이 코드 사용하면 됨
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    */
}
