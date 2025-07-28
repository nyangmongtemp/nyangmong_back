package com.playdata.boardservice.board.dto.req;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
import com.playdata.boardservice.common.util.HtmlSanitizer;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformationBoardSaveReqDto {

    
    private Category category;

    private String thumbnailImage;

    private String content;

    private  String title;

    public InformationBoard toEntity(Long userId, String nickname, String imageUrl, HtmlSanitizer htmlPolicy, HtmlSanitizer plainTextSanitizer) {
        return InformationBoard.builder()
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
