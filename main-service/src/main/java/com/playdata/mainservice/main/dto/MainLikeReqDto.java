package com.playdata.mainservice.main.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainLikeReqDto {

    @NotNull
    private Long contentId;

    @NotNull
    private String category;

}
