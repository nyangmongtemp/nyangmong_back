package com.playdata.boardservice.board.dto.req;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import com.playdata.boardservice.common.util.HtmlSanitizer;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroductionBoardSaveReqDto {

    private String thumbnailImage;

    private String content;

    private String title;

    public IntroductionBoard toEntity(Long userId, String nickname, String imageUrl, HtmlSanitizer htmlPolicy, HtmlSanitizer plainTextSanitizer) {
        return IntroductionBoard.builder()
                .thumbnailImage(imageUrl)
                .userId(userId)
                .nickname(nickname)
                .viewCount(0)
                .content(htmlPolicy.sanitizeHtml(content))
                .active(true)
                .title(plainTextSanitizer.sanitizeText(title))
                .build();
    }
}
