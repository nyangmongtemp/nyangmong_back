package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BoardSearchDto {

    private Long postId; // 게시물 번호
    private String title; // 게시글 제목
    private String nickname; // 닉네임
    private String content; // 본문 내용
    private Category category; // 카테고리


}
