package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyModiReqDto {

    private Long replyId;

    private String content;

}
