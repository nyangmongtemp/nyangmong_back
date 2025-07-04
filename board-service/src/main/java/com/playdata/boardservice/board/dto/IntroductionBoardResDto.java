package com.playdata.boardservice.board.dto;

import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroductionBoardResDto {

    private Long postid;
    private Long userid;
    private String thumbnailImage;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer viewCount;
    private String nickname;
    private String profileImage;


}
