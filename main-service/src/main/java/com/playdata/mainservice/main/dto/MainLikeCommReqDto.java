package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainLikeCommReqDto {

    private Long contentId;

    private String category;

    private String contentType;

}
