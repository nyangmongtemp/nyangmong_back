package com.playdata.mainservice.main.dto;

import com.playdata.mainservice.main.entity.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ComSaveResDto {

    private Long commentId;

    private Category category;

    private String content;

    private boolean hidden;

    private Long contentId;

    private Long userId;

    private String nickname;

    private String profileImage;

}
