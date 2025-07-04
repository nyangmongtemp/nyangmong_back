package com.playdata.mainservice.main.entity;

import com.playdata.mainservice.common.entity.BaseTimeEntity;
import com.playdata.mainservice.main.dto.ReplyDetailResDto;
import com.playdata.mainservice.main.dto.ReplySaveResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_reply")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reply extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    private Long userId;

    private String content;

    private boolean active;

    private String nickname;

    private String profileImage;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    // 대댓글 생성 시 사용하는 메소드
    public Reply(Long userId, String content, Comment comment, String nickname, String profileImage) {
        this.userId = userId;
        this.content = content;
        this.comment = comment;
        this.active = true;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public ReplyDetailResDto fromEntity(Long likeCount) {
        return ReplyDetailResDto.builder()
                .commentId(comment.getCommentId())
                .likeCount(likeCount)
                .replyId(replyId)
                .userId(userId)
                .content(content)
                .nickname(nickname)
                .profileImage(profileImage)
                .build();
    }

    // 대댓글 삭제 시 사용하는 메소드
    public void deleteReply() {
        this.active = false;
    }

    // 대댓글 수정 시 사용하는 메소드
    public void modifyReply(String content) {
        this.content = content;
    }

    // 사용자의 닉네임 변경 시 사용하는 메소드
    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }

    // 사용자의 프로필 이미지 변경 시 사용하는 메소드
    public void modifyProfileImage(String profileImage) {this.profileImage = profileImage;}
}
