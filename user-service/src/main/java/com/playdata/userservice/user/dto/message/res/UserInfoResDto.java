package com.playdata.userservice.user.dto.message.res;

import com.playdata.userservice.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserInfoResDto {

    private Long userId;

    private String username;

    private String nickname;

    private LocalDateTime createAt;

    private String profileImage;

}
