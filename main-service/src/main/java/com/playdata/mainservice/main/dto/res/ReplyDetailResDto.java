package com.playdata.mainservice.main.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReplyDetailResDto {

    private Long replyId;

    private String content;

    private Long commentId;

    private LocalDateTime createAt;

    private Long userId;

    private String nickname;

    private String profileImage;

    private Long likeCount;


}
