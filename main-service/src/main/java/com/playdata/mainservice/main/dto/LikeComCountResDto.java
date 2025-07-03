package com.playdata.mainservice.main.dto;

import com.playdata.mainservice.main.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LikeComCountResDto {

    private Long contentId;

    private String category;

    private Long likeCount;

    private Long commentCount;

}
