package com.playdata.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserLoginReqDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
