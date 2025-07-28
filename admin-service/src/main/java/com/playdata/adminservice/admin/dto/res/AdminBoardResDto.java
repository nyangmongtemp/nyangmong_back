package com.playdata.adminservice.admin.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminBoardResDto {
    private Long postId;
    private String category; // "INFORMATION", "INTRODUCTION", "ANIMAL"
    private Long userId;
    private String nickname;
    private String title;
    private String content;
    private String thumbnailImage;
    private int viewCount;
    private boolean active;
    private LocalDateTime createdAt;
}