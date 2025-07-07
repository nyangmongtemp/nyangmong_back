package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComModiReqDto {

    private String content;

    private Long commentId;
}
