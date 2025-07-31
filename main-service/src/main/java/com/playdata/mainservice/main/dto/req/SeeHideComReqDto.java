package com.playdata.mainservice.main.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "비공개 댓글 열람 요청 dto")
public class SeeHideComReqDto {

    @NotNull
    @Schema(description = "열람 요청을 받은 댓글 id", example = "1")
    // 열람하려는 댓글 아이디
    private Long commentId;

    @NotNull
    @Schema(description = "게시물 작성자 id", example = "1")
    // 게시물을 작성한 작성자 아이디
    private Long userId;

}
