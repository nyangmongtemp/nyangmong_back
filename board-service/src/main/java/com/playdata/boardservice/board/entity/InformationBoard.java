package com.playdata.boardservice.board.entity;

import com.playdata.boardservice.board.dto.InformationBoardResDto;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Getter @Setter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InformationBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId; // 게시글 번호

    @Column(nullable = false)
    private String category; // 게시판 종류

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 번호(?)

    @Column(name = "thumbnail_image")
    private String thumbnailImage; // 게시글 이미지 첨부 파일

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 본문 내용

    @Column(name = "view_count", nullable = false)
    private Integer viewCount; // 조회수

    @Column(nullable = false)
    private boolean active; // 게시글 업로드 상태(남아 있는지, 삭제 되었는지)

    private String nickname; // 사용자 닉네임

    private String profileImage; // 사용자 프로필 이미지

    public InformationBoardResDto fromEntity(InformationBoard Board) {
        return InformationBoardResDto.builder()
                .postid(postId)
                .category(category)
                .userid(userId)
                .thumbnailimage(thumbnailImage)
                .content(content)
                .createdat(getCreateTime())
                .updatedat(getUpdateTime())
                .viewcount(viewCount)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();

    }


}
