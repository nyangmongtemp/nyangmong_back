package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.InformationBoard;
import lombok.Getter;

@Getter
public class InformationBoardListResDto {

    private String title;
    private String content;
    private String nickname;
    private String thumbnailImage;
    private String category;

    public InformationBoardListResDto(InformationBoard informationBoard) {
        this.title = informationBoard.getTitle();
        this.content = informationBoard.getContent();
        this.nickname = informationBoard.getNickname();
        this.thumbnailImage = informationBoard.getThumbnailImage();
        this.category = informationBoard.getCategory();
    }



}
