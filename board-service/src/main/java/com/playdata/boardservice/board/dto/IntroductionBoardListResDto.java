package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IntroductionBoardListResDto {

    private Long postId;
    private String title;
    private String nickname;
    private String content;
    private String thumbnailImage;
    private int viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public IntroductionBoardListResDto(IntroductionBoard introductionBoard) {
        this.postId = introductionBoard.getPostId();
        this.title = introductionBoard.getTitle();
        this.nickname = introductionBoard.getNickname();
        this.content = introductionBoard.getContent();
        this.thumbnailImage = introductionBoard.getThumbnailImage();
        this.viewCount = introductionBoard.getViewCount();
        this.createAt = introductionBoard.getCreateAt();
        this.updateAt = introductionBoard.getUpdateAt();
    }

}
