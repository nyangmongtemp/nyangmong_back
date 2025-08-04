package com.playdata.adminservice.admin.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "비밀번호 요청 검증 DTO")
public class AdminPasswordAuthReqDto {

    @NotBlank
    @Schema(description = "인증코드", example = "1234")
    private String authCode;

}
