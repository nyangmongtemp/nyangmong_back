package com.playdata.adminservice.admin.dto.res;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminEmailAuthResDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String authCode;

}
