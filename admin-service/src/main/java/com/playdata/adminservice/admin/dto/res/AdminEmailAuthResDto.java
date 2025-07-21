package com.playdata.adminservice.admin.dto.res;

import com.playdata.adminservice.admin.dto.req.AdminPasswordAuthReqDto;
import com.playdata.adminservice.admin.entity.Admin;
import com.playdata.adminservice.common.auth.TokenUserInfo;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
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
