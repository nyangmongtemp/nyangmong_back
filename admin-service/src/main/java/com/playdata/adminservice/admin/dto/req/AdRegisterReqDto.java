package com.playdata.adminservice.admin.dto.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

/**
 * 광고 등록 및 수정 요청 DTO
 */
@Data
public class AdRegisterReqDto {


    // 광고 제목 (필수)
    @NotBlank(message = "광고 제목은 필수입니다.")
    private String title;

    // 광고 설명 (필수)
    @NotBlank(message = "광고 설명은 필수입니다.")
    private String description;

    //활성화 여부
    private Boolean active;

    // 광고 시작 날짜 (필수)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "광고 시작일은 필수입니다.")
    private LocalDate startDate;

    // 광고 종료 날짜 (필수)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "광고 종료일은 필수입니다.")
    private LocalDate endDate;

    // 광고 링크 주소
    private String linkUrl;

    // 광고 노출 필수 여부
    private Boolean confirmed;
}