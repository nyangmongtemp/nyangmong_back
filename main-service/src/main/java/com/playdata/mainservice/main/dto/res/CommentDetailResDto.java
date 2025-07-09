package com.playdata.mainservice.main.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentDetailResDto {

    private Long userId;

    private Long commentId;

    private String nickname;

    private Long contentId;

    private String category;

    private String content;

    private LocalDateTime createAt;

    private String profileImage;

    private Long likeCount;

    private boolean isReply;

    private boolean hidden;

}
