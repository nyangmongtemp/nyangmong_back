package com.playdata.mainservice.main.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "대댓글 생성 DTO")
public class ReplySaveReqDto {

    @Valid
    @Schema(description = "대댓글을 작성할 댓글 id", example = "1")
    private Long commentId;

    @Valid
    @Schema(description = "작성할 대댓글 내용", example = "작성된 대댓글 내용")
    private String content;

}
