package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroductionBoardSaveReqDto {

    private String thumbnailImage;
    private String content;


    public IntroductionBoard toEntity(Long userId, String nickname) {
        return IntroductionBoard.builder()
                .thumbnailImage(thumbnailImage)
                .userId(userId)
                .nickname(nickname)
                .viewCount(0)
                .content(content)
                .active(true)
                .build();
    }
}
