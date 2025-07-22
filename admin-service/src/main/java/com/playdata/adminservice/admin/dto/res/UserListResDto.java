package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserListResDto {

    private String userName;
    private String email;
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

    @Builder
    public UserListResDto(User user) {
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.profileImage = user.getProfileImage();
        this.nickname = user.getNickname();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.socialId = user.getSocialId();
        this.grade = user.getGrade();
        this.socialProvider = user.getSocialProvider();
        this.active = user.isActive();
        this.reportCount = user.getReportCount();
        this.passwordUpdatedAt = user.getPasswordUpdatedAt();
        this.passwordFaultCount = user.getPasswordFaultCount();
        this.pauseCount = user.getPauseCount();
    }

}
