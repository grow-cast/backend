package com.growcast.growcast.grownCrops;

import com.growcast.growcast.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "grownCrops")
@AllArgsConstructor
@NoArgsConstructor
public class GrownCrops {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gc_id", nullable = false)
    private Long gc_id;

    @Column(name = "name", nullable = false, length = 225)
    private String name;

    @Column(name = "nickname", nullable = true, length = 225)
    private String nickname;

    @Column(name="picture", columnDefinition = "TEXT", nullable = false)
    private String picture; //이미지 경로(gcs 사용 예정)

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
