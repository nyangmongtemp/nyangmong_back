package com.playdata.boardservice.board.dto.req;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeComCountReqDto {

    @NotNull
    private String category;

    @NotNull
    private Long contentId;


}
