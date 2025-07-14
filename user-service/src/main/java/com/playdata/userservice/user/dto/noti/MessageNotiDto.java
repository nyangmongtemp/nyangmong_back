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

    private long receiverId;

    public MessageNotiDto(String senderNickname, LocalDateTime sendTime, long senderId, long receiverId) {
        this.senderNickname = senderNickname;
        this.sendTime = sendTime;
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
}
