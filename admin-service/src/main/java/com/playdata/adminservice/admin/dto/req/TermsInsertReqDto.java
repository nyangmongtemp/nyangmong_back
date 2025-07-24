package com.playdata.adminservice.admin.dto.req;

import com.playdata.adminservice.admin.entity.Terms;
import com.playdata.adminservice.admin.entity.TermsCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Locale.Category;
import lombok.Getter;

/**
 * 약관/개인정보처리방침/QNA 등록 DTO
 */
@Getter
public class TermsInsertReqDto {

    @NotBlank
    private String title;
    @NotBlank
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