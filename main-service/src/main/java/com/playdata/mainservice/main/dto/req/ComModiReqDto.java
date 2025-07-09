package com.playdata.mainservice.main.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComModiReqDto {

    @NotNull
    private String content;

    @NotNull
    private Long commentId;
}
