package com.playdata.boardservice.board.dto.res;

import com.playdata.boardservice.board.entity.Board;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardListResDto extends BaseTimeEntity {

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
    public BoardListResDto(Board board) {
        this.postId = board.getPostId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.nickname = board.getNickname();
        this.thumbnailImage = board.getThumbnailImage();
        this.category = board.getCategory();
        this.viewCount = board.getViewCount();
        this.createAt = board.getCreateAt();
        this.updateAt = board.getUpdateAt();
    }

}
