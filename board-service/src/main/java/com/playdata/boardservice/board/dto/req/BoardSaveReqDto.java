package com.playdata.boardservice.board.dto.req;

import com.playdata.boardservice.board.entity.Board;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.common.util.HtmlSanitizer;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardSaveReqDto {

    private Category category;

    private String thumbnailImage;

    private String content;

    private  String title;

    public Board toEntity(Long userId, String nickname, String imageUrl, HtmlSanitizer htmlPolicy, HtmlSanitizer plainTextSanitizer) {
        return Board.builder()
                .userId(userId)
                .category(category)
                .thumbnailImage(imageUrl)
                .content(htmlPolicy.sanitizeHtml(content))
                .nickname(nickname)
                .viewCount(0)
                .active(true)
                .title(plainTextSanitizer.sanitizeText(title))
                .build();
    }
}
