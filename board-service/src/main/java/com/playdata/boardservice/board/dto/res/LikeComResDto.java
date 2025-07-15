package com.playdata.boardservice.board.dto.res;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class LikeComResDto { // 좋아요 수, 댓글 수 를 화면단으로 보내기 위한 용도

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

    // 좋아요 수
    private Long likeCount;

    // 댓글 수
    private Long commentCount;


    public static LikeComResDto fromEntity(InformationBoard Board, Long likeCount, Long commentCount) {
        return LikeComResDto.builder()
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
                .likeCount(likeCount)
                .commentCount(commentCount)
                .build();
    }

}
