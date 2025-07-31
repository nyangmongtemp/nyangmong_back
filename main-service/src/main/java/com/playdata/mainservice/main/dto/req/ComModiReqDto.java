package com.playdata.mainservice.main.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "댓글 수정 DTO")
public class ComModiReqDto {

    @NotNull
    @Schema(description = "수정할 내용", example = "수정된 댓글 내용")
    private String content;

    @NotNull
    @Schema(description = "댓글 id", example = "1")
    private Long commentId;
}
