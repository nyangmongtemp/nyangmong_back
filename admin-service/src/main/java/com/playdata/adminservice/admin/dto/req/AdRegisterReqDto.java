package com.playdata.adminservice.admin.dto.req;

import lombok.Data;
import java.time.LocalDate;

/**
 * 광고 등록 및 수정 요청 시 사용하는 DTO 클래스
 */
@Data
public class AdRegisterReqDto {

    // 광고 썸네일 이미지 URL
    private String thumbnailImage;

    // 광고 제목
    private String title;

    // 광고 설명
    private String description;

    // 광고 활성화 여부 (true: 활성, false: 비활성)
    private Boolean active;

    // 광고 노출 순서
    private Integer orderNum;

    // 광고 시작 날짜
    private LocalDate startDate;

    // 광고 종료 날짜
    private LocalDate endDate;
}