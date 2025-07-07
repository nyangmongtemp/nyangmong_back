package com.playdata.boardservice.board.dto;

import com.playdata.boardservice.board.entity.IntroductionBoard;
import lombok.Builder;
import lombok.Getter;

@Getter
public class IntroductionMainListResDto {
    private Long postId; // 게시물 번호
    private String thumbnailImage; // 썸네일 이미지
    private Integer viewCount; // 조회수
    private String nickname; // 사용자 닉네임
    private Long likeCount; // 좋아요 수
    private Long commentCount; // 댓글 수

    @Builder
    public IntroductionMainListResDto(IntroductionBoard introductionBoard, Long likeCount, Long commentCount) {
        this.postId = introductionBoard.getPostId();
        this.thumbnailImage = introductionBoard.getThumbnailImage();
        this.viewCount = introductionBoard.getViewCount();
        this.nickname = introductionBoard.getNickname();
        this.likeCount = likeCount;
        this.commentCount = commentCount;
    }
}
