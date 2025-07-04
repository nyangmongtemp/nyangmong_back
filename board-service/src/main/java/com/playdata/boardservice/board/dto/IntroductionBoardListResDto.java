package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IntroductionBoardListResDto {

    private String title;
    private String nickname;
    private String content;
    private String thumbnailImage;

    public IntroductionBoardListResDto(IntroductionBoard introductionBoard) {
        this.title = introductionBoard.getTitle();
        this.nickname = introductionBoard.getNickname();
        this.content = introductionBoard.getContent();
        this.thumbnailImage = introductionBoard.getThumbnailImage();
    }

}
