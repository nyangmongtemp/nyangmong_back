package com.playdata.userservice.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비밀번호 변경 요청 인증코드 DTO")
public class UserPwAuthReqDto {

    @NotBlank
    @Schema(description = "인증코드", example = "1234")
    String authCode;

}
