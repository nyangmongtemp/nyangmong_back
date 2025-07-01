package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.user.dto.UserInfoModiReqDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    private String profileImage;

    private String nickname;

    private String address;

    private String phone;

    private String socialId;

    private Long grade;   // 회원의 커뮤니티 활동을 기반으로 점수를 매길 것임.

    private String socialProvider;

    private boolean active;

    private int reportCount;

    private LocalDateTime passwordUpdatedAt;

    private int passwordFaultCount;

    private int pauseCount;

    // 인증이 필요하지 않은 사용자 정보를 수정하는 메소드
    public void modifyCommonUserInfo(UserInfoModiReqDto modiDto, String newProfileImage){
        this.profileImage = newProfileImage;
        this.nickname = modiDto.getNickname();
        this.address = modiDto.getAddress();
        this.phone = modiDto.getPhone();
    }

    // 인증이 필요한 이메일 정보를 변경하는 메소드
    public void modifyEmail(String newEmail){
        this.email = newEmail;
    }

    // 인증이 필요한 비밀번호를 변경하는 메소드
    public void modifyPassword(String newPassword){
        this.password = newPassword;
        this.passwordUpdatedAt = LocalDateTime.now();
    }

}
