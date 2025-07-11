package com.playdata.mainservice.main.entity;

import com.playdata.mainservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_like")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeId;

    private boolean active;

    private Long userId;

    private Long contentId;

    @Enumerated(EnumType.STRING)
    private Category category;
    
    // 새로 좋아요 데이터가 생성될 때, 사용할 생성자
    public Like(Long userId, Long contentId, Category category) {
        this.userId = userId;
        this.contentId = contentId;
        this.category = category;
        this.active = true;
    }

    // 좋아요 클릭 시 좋아요 취소 혹은 생성을 한 번에 처리하기 위한 메소드
    public void changeActive() {
        this.active = !this.active;
    }

    // 회원 탈퇴시 모든 좋아요 삭제
    public void deleteLike() {
        this.active = false;
    }

}
