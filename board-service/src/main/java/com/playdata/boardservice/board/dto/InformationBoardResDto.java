package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.InformationBoard;
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
    private String title;

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
                .profileImage(Board.getProfileImage())
                .title(Board.getTitle())
                .createdat(Board.getCreateTime())
                .updatedat(Board.getUpdateTime())
                .build();

    }
}
