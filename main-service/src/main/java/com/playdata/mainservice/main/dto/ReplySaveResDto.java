package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplySaveResDto {

    private Long replyId;

    private String content;

    private Long commentId;

    private Long userId;

    private String nickname;

    private String profileImage;

}
