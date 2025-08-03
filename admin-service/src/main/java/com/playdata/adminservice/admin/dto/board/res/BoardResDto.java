package com.playdata.adminservice.admin.dto.board.res;

import com.playdata.adminservice.admin.entity.Board;
import com.playdata.adminservice.admin.entity.Category;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardResDto {

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
    public static BoardResDto fromEntity(Board Board) {
        return BoardResDto.builder()
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
