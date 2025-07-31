package com.playdata.mainservice.main.dto.req;

import com.playdata.mainservice.main.dto.res.LikeComCountResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "게시물 상세 조회 시 댓글개수, 좋아요 개수 전달 dto")
public class LikeComCountReqDto {

    @NotNull
    @Schema(description = "조회할 게시물의 종류", example = "adopt")
    private String category;

    @NotNull
    @Schema(description = "조회할 게시물 id", example = "1")
    private Long contentId;

    // 게시물 상세 조회 시, 댓글 개수와 좋아요 수를 담은 dto 변환 메소드
    public LikeComCountResDto getLikeComCountResDto(Long count, long totalCount) {
        return LikeComCountResDto.builder()
                .contentId(this.getContentId())
                .category(this.getCategory())
                .likeCount(count)
                .commentCount(totalCount)
                .build();
    }

}
