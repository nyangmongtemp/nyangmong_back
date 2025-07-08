package com.playdata.boardservice.board.entity;

import com.playdata.boardservice.board.dto.BoardModiDto;
import com.playdata.boardservice.board.dto.InformationBoardResDto;
import com.playdata.boardservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class) // 서버 어플리케이션에 @EnableJpaAuditing를 불러올 수 있는 어노테이션
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
        this.thumbnailImage = newThumbnailImage;
        this.content = boardModiDto.getContent();
    }

    // 삭제
    public void boardDelete() {
        this.active = false;
    }

    public InformationBoardResDto fromEntity(InformationBoard Board) {
        return InformationBoardResDto.builder()
                .postid(postId)
                .category(category)
                .userid(userId)
                .thumbnailimage(thumbnailImage)
                .content(content)
                .createdat(getCreateAt())
                .updatedat(getUpdateAt())
                .viewcount(viewCount)
                .nickname(nickname)
                .title(title)
                .build();

    }
}
