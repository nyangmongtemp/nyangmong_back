package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.Category;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Schema(description = "게시물 생성 요청 DTO")
public class BoardSearchDto {

    @Schema(description = "게시물 id", example = "1")
    private Long postId; // 게시물 번호

    @Schema(description = " 제목", example = "게시물 제목")
    private String title; // 게시글 제목

    @Schema(description = "사용자 닉네임", example = "쾌도홍길동")
    private String nickname; // 닉네임

    @Schema(description = "게시물 내용", example = "게시물 내용")
    private String content; // 본문 내용

    @Schema(description = "카테고리", example = "FREE")
    private Category category; // 카테고리


}
