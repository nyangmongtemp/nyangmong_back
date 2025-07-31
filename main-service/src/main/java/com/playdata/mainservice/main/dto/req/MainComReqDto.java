package com.playdata.mainservice.main.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "댓글 생성 DTO")
public class MainComReqDto {

    @NotNull
    @Schema(description = "게시판 종류", example = "adopt")
    private String category;

    @NotNull
    @Schema(description = "비공개", example = "false")
    private boolean hidden;

    @NotNull
    @Schema(description = "댓글 내용", example = "댓글 내용 예시")
    private String content;

    @NotNull
    @Schema(description = "게시물 id", example = "1")
    private Long contentId;
}
