package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.entity.User;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserListResDto {

    private Long userId;
    private String userName;
    private String email;
    private String nickname;
    private boolean active;
    private int reportCount;
    private int pauseCount;
    private LocalDateTime createAt;

    @Builder
    public UserListResDto(User user) {
        this.userId = user.getUserId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.active = user.isActive();
        this.reportCount = user.getReportCount();
        this.pauseCount = user.getPauseCount();
        this.createAt = user.getCreateAt();
    }

}
