package com.playdata.mainservice.main.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReplyModiReqDto {

    @NotNull
    private Long replyId;

    @NotNull
    private String content;

}
