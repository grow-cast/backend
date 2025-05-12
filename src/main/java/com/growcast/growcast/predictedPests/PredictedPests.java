package com.growcast.growcast.predictedPests;

import com.growcast.growcast.grownCrops.GrownCrops;
import com.growcast.growcast.user.User;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "predictedPests")
@AllArgsConstructor
@NoArgsConstructor
public class PredictedPests {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pp_id", nullable = false)
    private long pp_id;

    @Column(name = "cropName", nullable = false, length = 225)
    private String cropName; //작물 이름

    @Column(name = "pestName", nullable = false, length = 225)
    private String pestName; //해충 이름

    @Enumerated(EnumType.STRING)
    @Column(name = "riskLevel", nullable = false)
    private RiskLevel riskLevel;

    @Column(name="forecastDetail", columnDefinition = "TEXT", nullable = false)
    private String forecastDetail; //날씨 디테일 정보

    @Column(name="responseGuide", columnDefinition = "TEXT", nullable = false)
    private String responseGuide; //가이드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name="createdAt", nullable = false)
    private LocalDateTime createdAt;

    /* 위 로컬데이터타입으로 생성할 때 오류가 발생하면 이 코드 사용하면 됨
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    */
}
