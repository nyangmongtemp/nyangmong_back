package com.playdata.adminservice.admin.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * 광고 개수(노출 수 등)를 관리하는 엔티티
 * advertisement_count 테이블과 매핑됨
 */
@Entity
@Table(name = "advertisement_count")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdvertisementCount {

    /**
     * 기본 키 ID (자동 증가)
     * DB 컬럼명은 ad_num_id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ad_num_id") // DB 컬럼명 유지
    private Long adNumId;

    /**
     * 광고 개수
     * DB 컬럼명: ad_num
     */
    @Column(name = "ad_num")
    private int adNum;

    /**
     * 생성자 - Builder 패턴 사용
     * @param adNum 광고 개수
     */
    @Builder
    public AdvertisementCount(int adNum) {
        this.adNum = adNum;
    }
}