package com.playdata.adminservice.admin.dto.board;

import com.playdata.adminservice.admin.entity.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
