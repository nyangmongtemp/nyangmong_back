package com.playdata.userservice.user.dto.res;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserEmailAuthResDto {

    @NotBlank
    private String email;

    @NotBlank
    private String authCode;

}
