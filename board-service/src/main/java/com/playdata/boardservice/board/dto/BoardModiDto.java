package com.playdata.boardservice.board.dto;

import lombok.*;

@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardModiDto {

    private String thumbnailImage;
    private String content;

}
