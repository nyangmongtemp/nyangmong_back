package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/**
 * 약관/개인정보처리방침/QNA 수정 DTO
 */
@Getter
@Schema(description = "약관/개인정보처리방침/QNA 수정 요청 DTO")
public class TermsUpdateReqDto {

    @NotBlank
    @Schema(description = "약관 제목", example = "이용약관 안내 - 수정본")
    private String title;

    @NotBlank
    @Schema(description = "약관 내용", example = "이용자는 본 약관을 반드시 확인해야 합니다. - 수정본")
    private String content;

}