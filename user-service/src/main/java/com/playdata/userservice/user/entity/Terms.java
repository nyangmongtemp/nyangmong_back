package com.playdata.userservice.user.entity;

import com.playdata.userservice.user.dto.req.TermsUpdateReqDto;
import com.playdata.userservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "tbl_terms")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Terms extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long termsId; // Terms 고유 ID (기본키, 자동 증가)

    @Column(nullable = false)
    private Long adminId; // 관리자id

    @Column(nullable = false)
    private String title; // 제목

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content; // 내용

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TermsCategory category; // 카테고리

    private Boolean active; // 활성화여부

    @PrePersist
    public void prePersist() {
        active = true;
    }


}