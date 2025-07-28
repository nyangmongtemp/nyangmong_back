package com.playdata.userservice.user.dto.report.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportSaveReqDto {

    // 신고 대상자
    @NotNull
    private Long userId;

    @NotBlank
    private String category;

    @NotBlank
    private String content;
}
