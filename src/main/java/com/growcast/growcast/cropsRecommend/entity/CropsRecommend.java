package com.growcast.growcast.cropsRecommend.entity;

import com.growcast.growcast.climate.entity.Climate;
import com.growcast.growcast.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "cropsRecommend")
@AllArgsConstructor
@NoArgsConstructor
public class CropsRecommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cr_id", nullable = false)
    private Long crrid;

    @Column(nullable = false, length = 225)
    private String recommendedCropName;

    @Column(name="reason", columnDefinition = "TEXT", nullable = false)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "climate_id", nullable = false)
    private Climate climate;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    /* 위 로컬데이터타입으로 생성할 때 오류가 발생하면 이 코드 사용하면 됨
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    */
}
