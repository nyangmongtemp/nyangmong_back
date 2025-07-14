package com.playdata.userservice.user.dto.noti;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MessageNotiDto {

    private String senderNickname;

    private LocalDateTime sendTime;
    
    private long senderId;  // 이건 필요 없을 수도

}
