package com.playdata.mainservice.main.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "대댓글 수정 DTO")
public class ReplyModiReqDto {

    @NotNull
    @Schema(description = "수정할 대댓글 id", example = "1")
    private Long replyId;

    @NotNull
    @Schema(description = "수정할 대댓글의 내용", example = "수정할 대댓글 내용")
    private String content;

}
