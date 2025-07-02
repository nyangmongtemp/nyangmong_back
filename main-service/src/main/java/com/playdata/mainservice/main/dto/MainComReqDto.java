package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainComReqDto {

    private String category;

    private boolean hidden;

    private String content;

    private Long contentId;
}
