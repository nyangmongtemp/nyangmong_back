package com.playdata.adminservice.admin.dto.res;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class AdminEmailAuthResDto {

    @NotBlank
    private String email;

    @NotBlank
    private String authCode;

    public AdminEmailAuthResDto(String email, String authCode) {
        this.email = email;
        this.authCode = authCode;
    }
}
