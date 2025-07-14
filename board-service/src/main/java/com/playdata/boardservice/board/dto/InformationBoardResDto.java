package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformationBoardResDto {

    private Long postid;
    private Category category;
    private Long userid;
    private String thumbnailimage;
    private String content;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;
    private Integer viewcount;
    private String nickname;
    private String title;

    @Builder
    public static InformationBoardResDto fromEntity(InformationBoard Board) {
        return InformationBoardResDto.builder()
                .postid(Board.getPostId())
                .category(Board.getCategory())
                .userid(Board.getUserId())
                .thumbnailimage(Board.getThumbnailImage())
                .content(Board.getContent())
                .title(Board.getTitle())
                .viewcount(Board.getViewCount())
                .nickname(Board.getNickname())
                .title(Board.getTitle())
                .createdat(Board.getCreateAt())
                .updatedat(Board.getUpdateAt())
                .build();

    }
}
