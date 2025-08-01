package com.playdata.userservice.user.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "이메일 인증 코드 DTO")
public class UserEmailAuthResDto {

    @NotBlank
    @Schema(description = "이메일", example = "example@example.com")
    private String email;

    @NotBlank
    @Schema(description = "인증코드", example = "1234")
    private String authCode;

}
