package com.growcast.growcast.scenarioInfo;

import com.growcast.growcast.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "scenarioInfo")
@AllArgsConstructor
@NoArgsConstructor
public class ScenarioInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scenario_id", nullable = false)
    private long scenario_id;

    @Column(name = "regionCode", nullable = false)
    private Integer regionCode;

    @Column(name="summary", columnDefinition = "TEXT", nullable = false)
    private String summary;

    @Column(name="recommendationNote", columnDefinition = "TEXT", nullable = true)
    private String recommendationNote;

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
