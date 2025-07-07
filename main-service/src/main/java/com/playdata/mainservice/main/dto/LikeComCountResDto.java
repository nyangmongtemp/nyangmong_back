package com.playdata.mainservice.main.dto;

import com.playdata.mainservice.main.entity.Comment;
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

}
