package com.playdata.userservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResDto {

    private String email;

    private String token;

}
