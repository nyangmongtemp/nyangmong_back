package com.playdata.schedulerservice.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * 지도데이터 정보 DB 엔티티.
 * JPA Entity 어노테이션 기반 매핑.
 *
 * updateIfChanged 메서드로 안전하게 필드 업데이트 가능.
 */
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "map")
@EntityListeners(AuditingEntityListener.class)
public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapId; // 지도 고유번호, PK 역할

    @Column(name = "addr1", nullable = false)
    private String addr1; // 주소

    @Column(name = "addr2")
    private String addr2; // 상세주소

    @Enumerated(EnumType.STRING)
    @Column(name = "address_code")
    private AddressCode addressCode; // 지역코드

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType; // 콘텐츠타입 (식당, 숙소 ...)

    @Column(name = "image1")
    private String image1; // 대표이미지

    @Column(name = "image2")
    private String image2; // 썸네일이미지

    @Column(name = "mapx")
    private String mapx; // x좌표

    @Column(name = "mapy")
    private String mapy; // y좌표

    @Column(name = "tel")
    private String tel; // 전화번호

    @Column(name = "title", nullable = false)
    private String title; // 제목

    @Column(name = "zipCode")
    private String zipCode; // 우편번호

    @Column(name = "content_id")
    private String contentId; // 고유값

    @CreatedDate
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt; // 생성 일시 (자동 관리)

    @LastModifiedDate
    @Column(name = "update_at")
    private LocalDateTime updateAt; // 수정 일시 (자동 관리)

    /**
     * 기존 엔티티 필드와 비교해 source 값이 다르고 null이 아닐 때만 업데이트
     * title, addr1, contentType 등 식별키 필드는 제외하여 안전한 필드 변경 보장
     */
    public void updateIfChanged(MapEntity source) {
        if (source.getAddr2() != null && !source.getAddr2().equals(this.addr2)) {
            this.addr2 = source.getAddr2();
        }
        if (source.getImage1() != null && !source.getImage1().equals(this.image1)) {
            this.image1 = source.getImage1();
        }
        if (source.getImage2() != null && !source.getImage2().equals(this.image2)) {
            this.image2 = source.getImage2();
        }
        if (source.getMapx() != null && !source.getMapx().equals(this.mapx)) {
            this.mapx = source.getMapx();
        }
        if (source.getMapy() != null && !source.getMapy().equals(this.mapy)) {
            this.mapy = source.getMapy();
        }
        if (source.getTel() != null && !source.getTel().equals(this.tel)) {
            this.tel = source.getTel();
        }
        if (source.getZipCode() != null && !source.getZipCode().equals(this.zipCode)) {
            this.zipCode = source.getZipCode();
        }
        if (source.getAddressCode() != null && !source.getAddressCode().equals(this.addressCode)) {
            this.addressCode = source.getAddressCode();
        }
        if (source.getContentType() != null && !source.getContentType().equals(this.contentType)) {
            this.contentType = source.getContentType();
        }
    }
}