package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "로그인 요청 DTO")
public class AdminLoginReqDto {

    @NotBlank
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "@Admin12345" )
    private String password;


}
