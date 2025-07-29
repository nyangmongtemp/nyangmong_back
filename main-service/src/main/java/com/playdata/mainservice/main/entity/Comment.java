package com.playdata.mainservice.main.entity;

import com.playdata.mainservice.common.entity.BaseTimeEntity;
import com.playdata.mainservice.main.dto.res.CommentDetailResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_comment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private Category category;

    private Long contentId;

    private String content;

    private boolean active;
    
    // 댓글의 비공개 여부  --> true: 비공개, false: 공개
    private boolean hidden;

    private String nickname;

    private String profileImage;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replyList;

    
    // 댓글 생성용 메소드
    public Comment(Long userId, Category category,
                   Long contentId, String content, boolean hidden, String nickname, String profileImage) {
        this.userId = userId;
        this.category = category;
        this.contentId = contentId;
        this.content = content;
        this.hidden = hidden;
        this.active = true;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }


    // 댓글 삭제 메소드
    public void deleteComment() {
        this.active = false;

        // 연관된 모든 대댓글도 비활성화
        if (replyList != null) {
            for (Reply reply : replyList) {
                reply.deleteReply();
            }
        }
    }

    // 댓글 수정 메소드
    public void mofifyComment(String newContent) {
        this.content = newContent;
    }

    // 사용자의 닉네임 변경 시 사용하는 메소드
    public void modifyNickname(String nickname) {
        this.nickname = nickname;

        // 댓글의 대댓글이 있는 경우
        if(replyList != null) {
            replyList.forEach(reply -> reply.modifyNickname(nickname));
        }
    }

    // 사용자의 프로필 이미지 변경 시 사용하는 메소드
    public void modifyProfileImage(String profileImage) {
        this.profileImage = profileImage;
        // 대댓글이 존재하는 경우
        if(replyList != null) {
            replyList.forEach(reply -> reply.modifyProfileImage(profileImage));
        }
    }

    // 대댓글 존재 여부 리턴 메소드
    public boolean isReplyExist() {
        if(replyList == null || replyList.isEmpty()) {
            return false;
        }
        else {
            for (Reply reply : replyList) {
                if(reply.isActive())
                    return true;
            }
        }
        return false;
    }

    // 게시물 상세 조회 시, 댓글의 정보들 + 좋아요 수 + 대댓글 여부를 담은 dto 변환 메소드
    public CommentDetailResDto getDetailResDto() {
        return CommentDetailResDto.builder()
                .contentId(this.getContentId())
                .category(String.valueOf(this.getCategory()))
                .content(this.getContent())
                // 대댓글 존재 여부
                .isReply(this.isReplyExist())
                .createAt(this.getCreateAt())
                .profileImage(this.getProfileImage())
                .nickname(this.getNickname())
                .userId(this.getUserId())
                // 비공개 여부
                .hidden(this.isHidden())
                .commentId(this.getCommentId())
                .build();
    }

}
