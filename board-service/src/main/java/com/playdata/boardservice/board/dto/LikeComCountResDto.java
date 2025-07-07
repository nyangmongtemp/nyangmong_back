package com.playdata.boardservice.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LikeComCountResDto {

    private Long contentId;

    private String category;

    private Long likeCount;

    private Long commentCount;


    // 메인화면에 드러날 소개 게시물의 좋아요, 댓글 개수, contentId 리턴용

}
