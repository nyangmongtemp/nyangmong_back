package com.playdata.mainservice.main.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeeHideComReqDto {

    @NotNull
    // 열람하려는 댓글 아이디
    private Long commentId;

    @NotNull
    // 게시물을 작성한 작성자 아이디
    private Long userId;

}
