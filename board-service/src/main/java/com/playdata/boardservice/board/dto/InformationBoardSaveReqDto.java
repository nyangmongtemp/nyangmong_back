package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.InformationBoard;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InformationBoardSaveReqDto {

    private String category;
    private String thumbnailImage;
    private String content;
    private  String title;


    public InformationBoard toEntity(Long userId, String nickname) {
        return InformationBoard.builder()
                .userId(userId)
                .category(category)
                .thumbnailImage(thumbnailImage)
                .content(content)
                .nickname(nickname)
                .viewCount(0)
                .active(true)
                .title(title)
                .build();
    }
}
