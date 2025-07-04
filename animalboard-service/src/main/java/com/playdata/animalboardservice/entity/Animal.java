package com.playdata.animalboardservice.entity;

import com.playdata.animalboardservice.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
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
    private Long postId; //pk 게시판 번호

    private Long userId; // 사용자 id 값

    @Column(name = "thumbnail_image", nullable = false)
    private String thumbnailImage; // 썸넹일 이미지

    private String title; // 제목

    private String content; // 내용

    private int viewCount; // 조회수

    private String petCategory; // 강아지, 고양이, 기타

    private String petKind; // 세부 분류 ( 믹스종 등)

    private String age; // 나이

    private String vaccine; // 백신접종여부

    @Column(name = "sex")
    private SexCode sexCode; // 성별

    @Column(name = "neuter")
    private NeuterYn neuterYn; // 중성화여부

    private String address; // 주소

    private String fee; // 책임비

    private boolean active; // 게시물 활성화

}
