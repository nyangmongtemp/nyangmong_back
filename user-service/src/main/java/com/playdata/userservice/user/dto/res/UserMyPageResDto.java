package com.playdata.userservice.user.dto.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserMyPageResDto {

    private String email;
    private String userName;
    private String nickname;
    private LocalDateTime createAt;
    private String phone;
    private String address;
    private String profileImage;

}
