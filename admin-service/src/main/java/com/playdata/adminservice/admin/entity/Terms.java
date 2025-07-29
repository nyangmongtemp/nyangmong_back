package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.admin.dto.req.TermsUpdateReqDto;
import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    @Transient
    private String adminName; // 관리자 이름

    @PrePersist
    public void prePersist() {
        active = true;
    }

    /**
     * 수정
     * @param reqDto
     */
    public void updateTerms(Long adminId, TermsUpdateReqDto reqDto) {
        this.adminId = adminId;
        this.title = reqDto.getTitle();
        this.content = reqDto.getContent();
    }

    /**
     * 삭제
     */
    public void deleteTerms() {
        this.active = false;
    }

}