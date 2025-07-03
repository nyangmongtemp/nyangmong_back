package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeComCountReqDto {

    private String category;

    private Long contentId;

}
