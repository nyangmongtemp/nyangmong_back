package com.playdata.mainservice.main.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MainComReqDto {

    @NotNull
    private String category;

    @NotNull
    private boolean hidden;

    @NotNull
    private String content;

    @NotNull
    private Long contentId;
}
