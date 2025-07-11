package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntroductionBoardSaveReqDto {

    private String thumbnailImage;

    private String content;

    private String title;

    public IntroductionBoard toEntity(Long userId, String nickname, String imageUrl) {
        return IntroductionBoard.builder()
                .thumbnailImage(imageUrl)
                .userId(userId)
                .nickname(nickname)
                .viewCount(0)
                .content(content)
                .active(true)
                .title(title)
                .build();
    }
}
