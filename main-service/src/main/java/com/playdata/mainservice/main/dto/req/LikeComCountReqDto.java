package com.playdata.mainservice.main.dto.req;

import com.playdata.mainservice.main.dto.res.LikeComCountResDto;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeComCountReqDto {

    @NotNull
    private String category;

    @NotNull
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
