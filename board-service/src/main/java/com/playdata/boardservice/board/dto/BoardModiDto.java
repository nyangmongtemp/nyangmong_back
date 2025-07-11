package com.playdata.boardservice.board.dto;

import lombok.*;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardModiDto {

    private String title;
    private String thumbnailImage;
    private String content;

}
