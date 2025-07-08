package com.playdata.mainservice.main.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeComCountReqDto {

    @NotNull
    private String category;

    @NotNull
    private Long contentId;

}
