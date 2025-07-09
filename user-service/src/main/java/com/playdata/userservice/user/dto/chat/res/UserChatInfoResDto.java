package com.playdata.userservice.user.dto.chat.res;

import com.playdata.userservice.user.dto.message.res.UserMessageResDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class UserChatInfoResDto {

    private Long chatId;

    private Long userId1;  // 요청자의 닉네임

    private Long userId2;  // 상대방의 닉네임

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private String nickname1;

    private String nickname2;

    private String requestNickname;

    private UserMessageResDto message;

}
