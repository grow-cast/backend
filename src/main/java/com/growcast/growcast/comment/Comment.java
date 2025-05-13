package com.growcast.growcast.comment;

import com.growcast.growcast.dashboard.Dashboard;
import com.growcast.growcast.user.entity.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Getter
@Setter
@Builder
@Table(name = "comment")
@AllArgsConstructor
@NoArgsConstructor

public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private long comment_id; //댓글 id

    @Column(name="content", columnDefinition = "TEXT", nullable = false)
    private String content; //댓글 내용

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", nullable = true)
    private Comment parent_comment_id; // 부모 댓글

    @Builder.Default
    @Column(name="hierarchy", nullable = false)
    private Integer hierarchy = 0; // 댓글 계층

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "dashboard", nullable = false)
    private Dashboard dashboard;

    @Column(name="createdAt", nullable = false)
    private LocalDateTime createdAt;

    /* 위 로컬데이터타입으로 생성할 때 오류가 발생하면 이 코드 사용하면 됨
    @Column(name = "created_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();
    */
}
