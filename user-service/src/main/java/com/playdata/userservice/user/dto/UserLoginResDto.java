package com.playdata.userservice.user.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginResDto {

    private String email;

    private String role;

    private boolean Logged;

}
