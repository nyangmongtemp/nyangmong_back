package com.playdata.boardservice.board.dto.res;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LikeComIntroResDto {

    private Long postid;
    private Long userid;
    private String thumbnailimage;
    private String content;
    private LocalDateTime createdat;
    private LocalDateTime updatedat;
    private Integer viewcount;
    private String nickname;
    private String title;

    // 좋아요 수
    private Long likeCount;

    // 댓글 수
    private Long commentCount;


    public static LikeComIntroResDto fromEntity(IntroductionBoard Board, Long likeCount, Long commentCount) {
        return LikeComIntroResDto.builder()
                .postid(Board.getPostId())
                .userid(Board.getUserId())
                .thumbnailimage(Board.getThumbnailImage())
                .content(Board.getContent())
                .title(Board.getTitle())
                .viewcount(Board.getViewCount())
                .nickname(Board.getNickname())
                .title(Board.getTitle())
                .createdat(Board.getCreateAt())
                .updatedat(Board.getUpdateAt())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

}
