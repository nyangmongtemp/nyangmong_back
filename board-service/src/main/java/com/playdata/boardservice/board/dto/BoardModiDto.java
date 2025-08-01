package com.playdata.boardservice.board.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시물 생성 요청 DTO")
public class BoardModiDto {

    @Schema(description = "수정될 제목", example = "수정될 게시물 제목")
    private String title;

    private String thumbnailImage;

    @Schema(description = "수정될 게시물 내용", example = "수정될 게시물 내용")
    private String content;

}
