package com.playdata.adminservice.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AdminLoginReqDto {

    @NotBlank
    private String email;

    @NotBlank
    private String password;


}
