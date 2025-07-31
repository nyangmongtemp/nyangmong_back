package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Locale.Category;
import lombok.Getter;

/**
 * 약관/개인정보처리방침/QNA 등록 DTO
 */
@Getter
@Schema(description = "약관/개인정보처리방침/QNA 등록 요청 DTO")
public class TermsInsertReqDto {

    @NotBlank
    @Schema(description = "약관 제목", example = "이용약관 안내")
    private String title;

    @NotBlank
    @Schema(description = "약관 내용", example = "이용자는 본 약관을 반드시 확인해야 합니다.")
    private String content;

    public Terms toEntity(Long adminId, TermsCategory category) {
        return Terms.builder()
                .adminId(adminId)
                .title(title)
                .content(content)
                .category(category)
                .build();
    }

}