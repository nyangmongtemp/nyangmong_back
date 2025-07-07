package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainLikeReqDto {

    private Long contentId;

    private String category;

    private String contentType;

}
