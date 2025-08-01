package com.playdata.boardservice.board.dto.req;

import com.playdata.boardservice.board.entity.Board;
import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.common.util.HtmlSanitizer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시물 생성 요청 DTO")
public class BoardSaveReqDto {

    @Schema(description = "카테고리", example = "FREE")
    private Category category;

    private String thumbnailImage;

    @Schema(description = "게시물 내용", example = "게시물 내용")
    private String content;

    @Schema(description = "제목", example = "게시물 제목")
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
