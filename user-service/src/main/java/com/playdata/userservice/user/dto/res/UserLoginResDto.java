package com.playdata.userservice.user.dto.res;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResDto {

    private String email;

    private String nickname;

    private String profileImage;

    private String token;

}
