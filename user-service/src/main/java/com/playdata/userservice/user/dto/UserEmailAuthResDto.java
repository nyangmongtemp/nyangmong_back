package com.playdata.userservice.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserEmailAuthResDto {

    private String email;

    private String authCode;

}
