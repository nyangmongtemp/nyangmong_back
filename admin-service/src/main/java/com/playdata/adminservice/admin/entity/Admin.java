package com.playdata.adminservice.admin.entity;

import com.playdata.adminservice.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_admin")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adminId; // 관리자 id

    @Column(nullable = false)
    private String email; // 관리자 email

    @Column(nullable = false)
    private String phone; // 전화번호

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role; // 관리자 직책

    @Column(nullable = false)
    private boolean active; // 활성상태

    @Column(nullable = false)
    private String name; // 관리자 이름

    @Column(nullable = false)
    private Boolean isFirst;

    // 디폴트 값 설정
    @PrePersist
    protected void onCreate() {
        this.active = true;
        this.isFirst = true;
    }

    public void modifyEmail(String newEmail){
        this.email = newEmail;
    }

    public void modifyPassword(String newPassword){
        this.password = newPassword;
    }







}
