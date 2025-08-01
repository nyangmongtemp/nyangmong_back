package com.playdata.userservice.user.dto.report.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "신고 생성 요청 DTO")
public class ReportSaveReqDto {

    // 신고 대상자
    @NotNull
    @Schema(description = "신고 대상 사용자 id", example = "2")
    private Long userId;

    @NotBlank
    @Schema(description = "신고 컨텐츠 카테고리", example = "BOARD")
    private String category;

    @NotBlank
    @Schema(description = "신고 내용", example = "신고 생성 예시 내용")
    private String content;
}
