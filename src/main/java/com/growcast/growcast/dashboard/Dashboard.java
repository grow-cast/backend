package com.growcast.growcast.dashboard;

import com.growcast.growcast.comment.Comment;
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
@Table(name = "dashboard")
@AllArgsConstructor
@NoArgsConstructor
public class Dashboard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dashboard_id", nullable = false)
    private long dashboard_id;

    @Column(name = "dashboardTitle", nullable = false, length = 225)
    private String dashboardTitle; //대시보드 제목

    @Column(name="picture", columnDefinition = "TEXT", nullable = false)
    private String picture; //이미지 경로(gcs 사용 예정)

    @Column(name = "cropName", nullable = true, length = 225)
    private String cropName;

    @Column(name="waterTime", nullable = true)
    private Integer waterTime; //물 준 시간

    @Column(name="waterAmount", nullable = true)
    private Integer waterAmount; //물 양

    @Column(name = "nutritionalSupplements", nullable = true, length = 225)
    private String nutritionalSupplements; //영양제

    @Column(name="nutritionalSupplementsTime", nullable = true)
    private Integer nutritionalSupplementsTime; //영양제 준 시간

    @Column(name="nutritionalSupplementsAmount", nullable = true)
    private Integer nutritionalSupplementsAmount; //영양제 양

    @Column(name = "usedPesticide", nullable = true, length = 225)
    private String usedPesticide; //사용한 농약

    @Column(name="cropGrowthStatus", columnDefinition = "TEXT", nullable = false)
    private String cropGrowthStatus; //작물 생장 상태

    @Column(name="number", nullable = true)
    private Integer number; //밭에 나간 횟수

    @Column(name="workHistory", columnDefinition = "TEXT", nullable = false)
    private String workHistory; //작업 내용

    @Builder.Default
    @Column(name="harvesMonth", nullable = true)
    private LocalDate harvesMonth = LocalDate.now(); //수확일

    @Column(name="harvesAmount", nullable = true)
    private Integer harvesAmount; //수확량

    @Column(name = "harvesState", nullable = true, length = 225)
    private String harvesState; //수확 상태

    @Column(name = "StorageMethod", nullable = true, length = 225)
    private String storageMethod; //저장 방법

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "gc_id", nullable = true)
    private GrownCrops grownCrops;

    @Column(name="createdAt", nullable = false)
    private LocalDateTime createdAt;

    /* 위 로컬데이터타입으로 생성할 때 오류가 발생하면 이 코드 사용하면 됨
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    */
}