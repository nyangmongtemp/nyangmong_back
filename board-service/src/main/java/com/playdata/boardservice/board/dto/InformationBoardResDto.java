package com.playdata.boardservice.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformationBoardResDto {

    private Long postid;
    private String category;
    private Long userid;
    private String thumbnailimage;
    private String content;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;
    private Integer viewcount;
    private String nickname;
    private String profileImage;

}
