package com.playdata.mainservice.main.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeComCountResDto {

    private Long contentId;

    private String category;

    private Long commentCount;

    private Long likeCount;
}
