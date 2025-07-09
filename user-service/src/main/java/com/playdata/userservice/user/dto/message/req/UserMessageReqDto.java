package com.playdata.userservice.user.dto.message.req;

import com.playdata.userservice.user.entity.Message;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserMessageReqDto {

    @NotNull
    private Long receiverId;  // 또는 email?

    @NotBlank
    private String content;



}
