package com.playdata.userservice.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginReqDto {

    private String email;

    private String password;

}
