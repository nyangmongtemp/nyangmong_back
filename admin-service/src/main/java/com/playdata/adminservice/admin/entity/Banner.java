package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.admin.dto.banner.req.BannerSaveReqDto;
import com.playdata.adminservice.admin.dto.banner.res.BannerListResDto;
import com.playdata.adminservice.admin.dto.banner.res.BannerSaveResDto;
import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_banner")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Banner extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bannerId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Long adminId;

    @Column(nullable = false, length = 1000)
    private String thumbnailImage;
    
    // order는 작을 수록 먼저 노출됨
    @Column
    private Integer orderNum;

    @Column(nullable = false)
    private boolean active;

    @Column(nullable = false)
    private boolean basic;
    
    // 요청을 받은 배너 생성자
    public Banner(Long adminId, BannerSaveReqDto reqDto, Integer order) {
        this.adminId = adminId;
        this.title = reqDto.getTitle();
        this.thumbnailImage = reqDto.getThumbnailImage();
        this.active = true;
        this.basic = false;
        this.orderNum = order;
    }

    // default 배너 생성용 생성자
    public Banner(String title, Long adminId, String thumbnailImage, Integer order) {
        this.title = title;
        this.adminId = adminId;
        this.thumbnailImage = thumbnailImage;
        this.orderNum = order;
        this.active = true;
        this.basic = true;
    }

    // 이미지 수정용 메소드
    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    // 제목 수정용 메소드
    public void setTitle(String title) {
        this.title = title;
    }

    // 순서 수정용 메소드
    public void setOrder(Integer order) {
        this.orderNum = order;
    }
    
    // 수정 시 최종 수정한 관리자 갱신용 메소드
    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    // 배너 삭제용 메소드
    public void deleteBanner() {
        this.active = false;
    }

    // 노출 해제 또는 순서 변경용 메소드
    public void changeOrder(Integer order) {
        this.orderNum = order;
    }

    // 상세 정보 조회 dto 변환
    public BannerSaveResDto toDetailDto() {
        return BannerSaveResDto.builder()
                .bannerId(bannerId)
                .title(title)
                .adminId(adminId)
                .thumbnailImage(thumbnailImage)
                .order(orderNum)
                .basic(basic)
                .build();
    }
    
    // 목록 조회 dto 변환
    public BannerListResDto toListDto() {
        return BannerListResDto.builder()
                .bannerId(bannerId)
                .title(title)
                .adminId(adminId)
                .order(orderNum)
                .basic(basic)
                .image(thumbnailImage)
                .build();
    }

}
