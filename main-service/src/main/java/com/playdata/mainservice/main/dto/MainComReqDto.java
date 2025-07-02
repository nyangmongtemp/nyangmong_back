package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainComReqDto {

    private String category;

    private String contentType;

    private boolean hidden;

    private String content;

    private Long contentId;
}
