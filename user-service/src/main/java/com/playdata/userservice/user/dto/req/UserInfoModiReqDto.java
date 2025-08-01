package com.playdata.userservice.user.dto.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "회원 정보 변경 요청 DTO")
public class UserInfoModiReqDto {
    
    // 없어도 됨
    @Schema(description = "닉네임", example = "쾌걸홍길동")
    String nickname;

}
