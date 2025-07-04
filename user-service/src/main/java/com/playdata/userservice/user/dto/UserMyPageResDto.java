package com.playdata.userservice.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UserMyPageResDto {

    private String email;
    private String userName;
    private String nickname;
    private LocalDateTime createtime;
    private String phone;
    private String address;

}
