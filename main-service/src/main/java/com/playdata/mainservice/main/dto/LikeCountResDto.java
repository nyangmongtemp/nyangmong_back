package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeCountResDto {

    private Long contentId;

    private String contentType;

    private String category;

    private Long likeCount;

}
