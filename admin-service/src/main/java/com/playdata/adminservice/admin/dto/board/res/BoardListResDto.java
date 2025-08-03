package com.playdata.adminservice.admin.dto.board.res;

import com.playdata.adminservice.admin.entity.Board;
import com.playdata.adminservice.admin.entity.Category;
import com.playdata.adminservice.common.entity.BaseTimeEntity;
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
    public BoardListResDto(Long postId, String title, String content, String nickname, String thumbnailImage, Category category, int viewCount, LocalDateTime createAt, LocalDateTime updateAt) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.thumbnailImage = thumbnailImage;
        this.category = category;
        this.viewCount = viewCount;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

}
