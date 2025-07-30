package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.admin.dto.req.AdUpdateReqDto;
import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * 광고(Advertisement) 엔티티 클래스
 * - 광고 정보에 대한 DB 매핑을 담당함
 * - BaseTimeEntity를 상속하여 생성일/수정일 자동 관리
 */
@Entity
@Table(name = "advertisements")
@Getter
@NoArgsConstructor
public class Advertisement extends BaseTimeEntity {

    /** 광고 ID (기본 키) */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "advertisement_id")
    private Long id;

    /** 썸네일 이미지 URL */
    @Column(nullable = false, length = 500)
    private String thumbnailImage;

    /** 광고 제목 */
    @Column(nullable = false, length = 100)
    private String title;

    /** 광고 설명 */
    @Column(nullable = false, length = 1000)
    private String description;

    /** 광고 승인 여부 (관리자 승인 여부) */
    private Boolean confirmed;

    /** 광고 활성화 여부 (true: 노출 중, false: 비노출) */
    @Setter
    @Column(nullable = false)
    private Boolean active = true;

    /** 광고 시작 날짜 */
    @Column(nullable = false)
    private LocalDate startDate;

    /** 광고 종료 날짜 */
    @Column(nullable = false)
    private LocalDate endDate;

    /** 클릭 시 이동할 외부 링크 URL */
    @Column(name = "link_url")
    private String linkUrl;

    /**
     * 광고 정보 수정 메서드
     * - 주로 수정 요청 DTO와 썸네일 이미지 경로를 기반으로 변경
     *
     * @param dto 수정 요청 DTO
     * @param thumbnailImage 새로운 썸네일 이미지 URL
     */
    public void update(AdUpdateReqDto dto, String thumbnailImage) {
        this.title = dto.getTitle();
        this.description = dto.getDescription();
        this.confirmed = dto.getConfirmed();
        this.active = dto.getActive();
        this.startDate = dto.getStartDate();
        this.endDate = dto.getEndDate();
        this.linkUrl = dto.getLinkUrl();
        this.thumbnailImage = thumbnailImage;
    }

    /**
     * Advertisement 생성자 - Builder 패턴 지원
     *
     * @param thumbnailImage 썸네일 이미지 URL
     * @param title 광고 제목
     * @param description 광고 설명
     * @param confirmed 승인 여부
     * @param active 활성화 여부
     * @param startDate 시작일
     * @param endDate 종료일
     * @param linkUrl 링크 URL
     */
    @Builder
    public Advertisement(String thumbnailImage, String title, String description, Boolean confirmed,
                         Boolean active, LocalDate startDate, LocalDate endDate, String linkUrl) {
        this.thumbnailImage = thumbnailImage;
        this.title = title;
        this.description = description;
        this.active = active;
        this.confirmed = confirmed;
        this.startDate = startDate;
        this.endDate = endDate;
        this.linkUrl = linkUrl;
    }

    /**
     * 엔티티 저장 전 기본값 설정
     * - active가 null이면 true로 초기화
     * - confirmed가 null이면 false로 초기화
     */
    @PrePersist
    protected void onCreate() {
        if (this.active == null) {
            this.active = true;
        }
        if (this.confirmed == null) {
            this.confirmed = false;
        }
    }
}