package com.playdata.boardservice.board.entity;

import com.playdata.boardservice.board.dto.BoardModiDto;
import com.playdata.boardservice.board.dto.IntroductionBoardResDto;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
public class IntroductionBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long postId; // 게시물 번호

    @Column(name = "user_id", nullable = false)
    private Long userId; // 사용자 번호(?)

    @Column(name = "thumbnail_image", nullable = false)
    private String thumbnailImage; // 썸네일 이미지

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; // 게시글 본문

    @Column(name = "view_count", nullable = false)
    private Integer viewCount; // 조회수

    @Column(nullable = false)
    private boolean active; // 게시글 업로드 상태 (남아 있는지, 삭제 되었는지)

    @Column(nullable = false)
    private String nickname; // 사용자 닉네임

    @Column(nullable = false)
    private String title; // 게시글 제목

    // 디폴트 값 설정
    @PrePersist
    protected void onCreate() {
        this.active = true;
        this.viewCount = 0;
    }

    // 조회수 증가
    public void viewCountUp(int viewCount) {
        this.viewCount = viewCount;
    }

    // 수정
    public void boardModify(BoardModiDto boardModiDto, String newThumbnailImage) {
        this.content = boardModiDto.getContent();
        this.thumbnailImage = newThumbnailImage;
    }

    // 삭제
    public void boardDelete() {
        this.active = false;
    }

    public IntroductionBoardResDto fromEntity(IntroductionBoard Board) {
        return IntroductionBoardResDto.builder()
                .postid(postId)
                .userid(userId)
                .thumbnailImage(thumbnailImage)
                .content(content)
                .createdAt(getCreateAt())
                .updatedAt(getUpdateAt())
                .viewCount(viewCount)
                .nickname(nickname)
                .title(title)
                .build();
    }

}
