package com.playdata.adminservice.admin.entity;


import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class) // 서버 어플리케이션에 @EnableJpaAuditing를 불러올 수 있는 어노테이션
public class InformationBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId; // 게시글 번호

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private Category category; // 게시판 종류

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 번호(?)

    @Column(name = "thumbnail_image")
    private String thumbnailImage; // 게시글 이미지 첨부 파일

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 본문 내용

    @Column(name = "view_count", nullable = false)
    private Integer viewCount; // 조회수

    @Column(nullable = false)
    private boolean active; // 게시글 업로드 상태(남아 있는지, 삭제 되었는지)

    @Column(nullable = false)
    private String nickname; // 사용자 닉네임

    @Column(nullable = false)
    private String title; // 게시글 제목\

    public void setActive(boolean active) {
        this.active = active;
    }
}
