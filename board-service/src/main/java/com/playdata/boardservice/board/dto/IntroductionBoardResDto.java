package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import lombok.*;

import java.time.LocalDateTime;


@Getter
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
    private String title;

    public static IntroductionBoardResDto fromEntity(IntroductionBoard Board) {
        return IntroductionBoardResDto.builder()
                .postid(Board.getPostId())
                .userid(Board.getUserId())
                .thumbnailImage(Board.getThumbnailImage())
                .content(Board.getContent())
                .title(Board.getTitle())
                .viewCount(Board.getViewCount())
                .nickname(Board.getNickname())
                .createdAt(Board.getCreateAt())
                .updatedAt(Board.getUpdateAt())
                .build();
    }


}
