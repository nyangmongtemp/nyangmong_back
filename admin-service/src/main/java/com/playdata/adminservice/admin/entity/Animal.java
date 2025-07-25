package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animal")
@EntityListeners(AuditingEntityListener.class)
public class Animal extends BaseTimeEntity {

    @Id
    @Column(name = "post_id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId; //pk 게시판 번호

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 id 값

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "thumbnail_image", nullable = false)
    private String thumbnailImage; // 썸넹일 이미지

    @Column(name = "title", nullable = false)
    private String title; // 제목

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // 내용

    @Column(name = "view_count")
    private int viewCount; // 조회수

    @Column(name = "pet_category", nullable = false)
    private String petCategory; // 강아지, 고양이, 기타

    @Column(name = "pet_kind", nullable = false)
    private String petKind; // 세부 분류 ( 믹스종 등 )

    @Column(name = "age", nullable = false)
    private String age; // 나이

    @Column(name = "vaccine")
    private String vaccine; // 백신접종여부

    @Enumerated(EnumType.STRING)
    @Column(name = "sex", nullable = false)
    private SexCode sexCode; // 성별

    @Enumerated(EnumType.STRING)
    @Column(name = "neuter", nullable = false)
    private NeuterYn neuterYn; // 중성화여부

    @Column(name = "address", nullable = false)
    private String address; // 주소

    @Column(name = "fee", nullable = false)
    private Integer fee; // 책임비

    @Column(name = "active")
    private boolean active; // 게시물 활성화

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_status")
    private ReservationStatus reservationStatus; // 예약

    public void setActive(boolean active) {
        this.active = active;
    }
}
