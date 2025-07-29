package com.playdata.userservice.user.dto.req;

import com.playdata.userservice.user.entity.Terms;
import com.playdata.userservice.user.entity.TermsCategory;
import jakarta.validation.constraints.NotBlank;
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