package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
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

    // 사용자 이름
    @Transient
    private String userName;

    // 사용자 이메일
    @Transient
    private String userEmail;

    // 답변
    public void makeReply(String reply, Long adminId) {
        this.reply = reply;
        this.adminId = adminId;
        this.answered = true;
    }

}
