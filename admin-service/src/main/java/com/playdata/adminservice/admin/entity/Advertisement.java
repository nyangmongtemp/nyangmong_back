package com.playdata.adminservice.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 광고 정보(Entity)
 * DB의 advertisements 테이블과 매핑됨
 */
@Entity
@Table(name = "advertisements")
@Getter
@NoArgsConstructor
public class Advertisement {

    // 광고 식별자 (PK), DB에서는 banner_id 컬럼으로 저장됨
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "banner_id")
    private Long id;

    // 광고 썸네일 이미지 URL
    private String thumbnailImage;

    // 광고 제목
    private String title;

    // 광고 설명
    private String description;

    // 광고 활성화 여부 (true: 노출, false: 비노출)
    private Boolean active;

    // 광고 순서 (정렬에 사용됨)
    private Integer orderNum;

    // 광고 시작일
    private LocalDate startDate;

    // 광고 종료일
    private LocalDate endDate;

    // 광고 생성일시
    private LocalDateTime createdAt;

    // 광고 최종 수정일시
    private LocalDateTime updatedAt;

    /**
     * 엔티티가 저장되기 전에 호출됨 (자동 생성일 설정)
     */
    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 엔티티가 업데이트되기 전에 호출됨 (자동 수정일 갱신)
     */
    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * 광고 정보 수정 메서드
     *
     * @param title 광고 제목
     * @param description 광고 설명
     * @param active 광고 활성화 여부
     * @param orderNum 광고 정렬 순서
     * @param thumbnailImage 썸네일 이미지 URL
     * @param startDate 광고 시작일
     * @param endDate 광고 종료일
     */
    public void update(String title, String description, Boolean active, Integer orderNum,
                       String thumbnailImage, LocalDate startDate, LocalDate endDate) {

        this.title = title;
        this.description = description;
        this.active = active;
        this.orderNum = orderNum;
        this.thumbnailImage = thumbnailImage;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}