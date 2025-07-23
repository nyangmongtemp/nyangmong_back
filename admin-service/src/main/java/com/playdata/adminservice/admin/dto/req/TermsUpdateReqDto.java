package com.playdata.adminservice.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 약관/개인정보처리방침/QNA 수정 DTO
 */
@Getter
public class TermsUpdateReqDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;

}