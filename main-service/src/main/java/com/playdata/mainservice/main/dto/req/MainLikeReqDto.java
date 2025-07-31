package com.playdata.mainservice.main.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "좋아요 생성 및 취소 DTO")
public class MainLikeReqDto {

    @NotNull
    @Schema(description = "게시물 id", example = "1")
    private Long contentId;

    @NotNull
    @Schema(description = "게시판 종류", example = "adopt")
    private String category;

}
