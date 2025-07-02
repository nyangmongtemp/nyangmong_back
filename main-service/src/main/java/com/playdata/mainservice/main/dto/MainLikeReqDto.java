package com.playdata.mainservice.main.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainLikeReqDto {

    Long contentId;

    String category;

    String contentType;

}
