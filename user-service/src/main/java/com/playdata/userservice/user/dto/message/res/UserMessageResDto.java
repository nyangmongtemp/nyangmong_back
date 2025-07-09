package com.playdata.userservice.user.dto.message.res;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserMessageResDto {

    private Long senderId;

    private Long receiverId;

    private Long messageId;

    private String content;

    private LocalDateTime createAt;

    private boolean active;

    private Long chatId;

    private boolean readed;

    private String nickname1;

    private String nickname2;

    private String requestNickname;

}
