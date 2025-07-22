package com.playdata.adminservice.admin.dto.req;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * 광고 등록 및 수정 요청 DTO
 */
@Data
public class AdRegisterReqDto {

    // 광고 썸네일 이미지 URL (필수)
    @NotBlank(message = "썸네일 이미지는 필수입니다.")
    private String thumbnailImage;

    // 광고 제목 (필수)
    @NotBlank(message = "광고 제목은 필수입니다.")
    private String title;

    // 광고 설명 (필수)
    @NotBlank(message = "광고 설명은 필수입니다.")
    private String description;

    //활성화 여부
    private Boolean active;

    // 광고 시작 날짜 (필수)
    @NotNull(message = "광고 시작일은 필수입니다.")
    private LocalDate startDate;

    // 광고 종료 날짜 (필수)
    @NotNull(message = "광고 종료일은 필수입니다.")
    private LocalDate endDate;

    // 광고 링크 주소
    private String linkUrl;

    // 광고 노출 필수 여부
    private Boolean confirmed;
}