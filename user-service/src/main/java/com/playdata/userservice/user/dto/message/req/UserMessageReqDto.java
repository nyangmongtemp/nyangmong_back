package com.playdata.userservice.user.dto.message.req;

import com.playdata.userservice.user.entity.Message;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "쪽지 생성 요청 DTO")
public class UserMessageReqDto {

    @NotNull
    @Schema(description = "쪽지 받는 사용자 id", example = "2")
    private Long receiverId;  // 또는 email?

    @NotBlank
    @Schema(description = "보낼 쪽지 내용", example = "쪽지 생성 예시")
    private String content;



}
