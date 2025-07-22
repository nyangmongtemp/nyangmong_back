package com.playdata.adminservice.admin.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AdminPasswordAuthReqDto {

    @NotBlank
    private String authCode;

}
