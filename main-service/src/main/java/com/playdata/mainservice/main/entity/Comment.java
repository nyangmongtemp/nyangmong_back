package com.playdata.mainservice.main.entity;

import com.playdata.mainservice.common.entity.BaseTimeEntity;
import com.playdata.mainservice.main.dto.ComSaveResDto;
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

    private boolean hidden;

    @OneToMany(mappedBy = "comment")
    private List<Reply> replyList;

    
    // 댓글 생성용 메소드
    public Comment(Long userId, Category category, Long contentId, String content, boolean hidden) {
        this.userId = userId;
        this.category = category;
        this.contentId = contentId;
        this.content = content;
        this.hidden = hidden;
        this.active = true;
    }

    // 댓글 생성 및 수정 시 리턴할 Dto 변환 메소드
    public ComSaveResDto fromEntity() {
        return ComSaveResDto.builder()
                .commentId(commentId)
                .userId(userId)
                .category(category)
                .contentId(contentId)
                .content(content)
                .hidden(hidden)
                .build();
    }

    // 댓글 삭제 메소드
    public void deleteComment() {
        this.active = false;
    }

    // 댓글 수정 메소드
    public void mofifyComment(String newContent) {
        this.content = newContent;
    }
}
