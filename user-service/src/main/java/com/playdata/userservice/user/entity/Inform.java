package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_inform")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inform extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long informId;
    
    // 문의 작성자 (사용자)
    private Long userId;
    
    // 답변 작성자 (관리자)
    private Long adminId;
    
    // 문의 제목
    private String title;
    
    // 문의 내용
    private String content;
    
    // 삭제 여부
    private boolean active;
    
    // 답변 여부
    private boolean answered;
    
    // 답변 내용
    private String reply;

    // 문의 생성용 생성자
    public Inform(Long userId, String title, String content) {
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.active = true;
        this.answered = false;
        this.reply = null;
        this.adminId = null;
    }

    // 문의 수정용 메소드
    public void modifyInform(String title, String content) {
        // 수정할 데이터가 오지 않은 경우
        if(title != null && content != null) {
            throw new CommonException(ErrorCode.BAD_REQUEST);
        }
        if (title != null) {
            this.title = title;
        }
        if (content != null) {
            this.content = content;
        }
    }

    // 문의 삭제용 메소드
    public void deleteInform() {
        this.active = false;
    }

/////////////////  관리자용 메소드

    // 답변 생성
    public void makeReply(String reply, Long adminId) {
        this.reply = reply;
        this.adminId = adminId;
        this.answered = true;
    }

    // 답변 수정
    public void changeReply(String reply) {
        this.reply = reply;
        // 혹시 몰라서
        this.answered = true;
    }

}
