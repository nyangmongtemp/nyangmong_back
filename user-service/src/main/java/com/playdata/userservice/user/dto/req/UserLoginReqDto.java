package com.playdata.userservice.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@Schema(description = "로그인 DTO")
public class UserLoginReqDto {

    @NotBlank
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank
    @Schema(description = "비밀번호", example = "Abcdefgh1!")
    private String password;

}
