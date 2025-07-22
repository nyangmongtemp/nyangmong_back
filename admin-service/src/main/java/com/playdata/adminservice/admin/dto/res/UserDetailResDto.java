package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDetailResDto {

    private String userName;
    private String email;
    private String nickname;
    private String address;
    private String phone;
    private String socialId;
    private String socialProvider;
    private boolean active;
    private int reportCount;
    private int pauseCount;

    @Builder
    public UserDetailResDto(User user) {
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.socialId = user.getSocialId();
        this.socialProvider = user.getSocialProvider();
        this.active = user.isActive();
        this.reportCount = user.getReportCount();
        this.pauseCount = user.getPauseCount();
    }

}
