package com.playdata.boardservice.board.dto.req;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.board.entity.InformationBoard;
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

    public InformationBoard toEntity(Long userId, String nickname,String imageUrl) {
        return InformationBoard.builder()
                .userId(userId)
                .category(category)
                .thumbnailImage(imageUrl)
                .content(content)
                .nickname(nickname)
                .viewCount(0)
                .active(true)
                .title(title)
                .build();
    }
}
