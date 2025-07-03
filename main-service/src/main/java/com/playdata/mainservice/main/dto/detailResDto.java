package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class detailResDto {

    private Long userId;

    private Long commentId;

    private String nickname;

    private Long contentId;

    private String category;

    private String content;

    private LocalDateTime createTime;

    private Long likeCount;

    private boolean isReply;

}
