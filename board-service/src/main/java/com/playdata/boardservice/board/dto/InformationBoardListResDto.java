package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class InformationBoardListResDto extends BaseTimeEntity{

    private Long postId;
    private String title;
    private String content;
    private String nickname;
    private String thumbnailImage;
    private Category category;
    private int viewCount;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    @Builder
    public InformationBoardListResDto(InformationBoard informationBoard) {
        this.postId = informationBoard.getPostId();
        this.title = informationBoard.getTitle();
        this.content = informationBoard.getContent();
        this.nickname = informationBoard.getNickname();
        this.thumbnailImage = informationBoard.getThumbnailImage();
        this.category = informationBoard.getCategory();
        this.viewCount = informationBoard.getViewCount();
        this.createAt = informationBoard.getCreateAt();
        this.updateAt = informationBoard.getUpdateAt();
    }



}
