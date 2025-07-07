package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SeeHideComReqDto {
    
    // 열람하려는 댓글 아이디
    private Long commentId;
    // 게시물을 작성한 작성자 아이디
    private Long userId;

}
