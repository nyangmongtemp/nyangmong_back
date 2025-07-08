package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.user.dto.message.res.UserMessageResDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
// 임의로 지정한 테이블 이름 -> 추후에 모든 서비스의 테이블 이름을 통일할 것!
@Table(name = "tbl_message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long messageId;

    // 발신자
    private Long senderId;

    // 수신자
    private Long receiverId;

    private String content;

    private boolean active;
    
    // 상대방의 열람 여부
    private boolean readed;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    // 메시지 생성자
    public Message(Long senderId, Long receiverId, String content, Chat chat) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.chat = chat;
        this.readed = false;
        this.active = true;
    }

    // 메시지 정보 리턴 dto 변환
    public UserMessageResDto fromEntity(String nickname1, String nickname2, String requestNickname) {
        return UserMessageResDto.builder()
                .messageId(getMessageId())
                .senderId(getSenderId())
                .receiverId(getReceiverId())
                .content(getContent())
                .active(isActive())
                .readed(isReaded())
                .chatId(chat.getChatId())
                .createAt(this.getCreateAt())
                .nickname1(nickname1)
                .nickname2(nickname2)
                .requestNickname(requestNickname)
                .build();
    }

    // 메시지 읽음 처리
    public void setRead() {
        this.readed = true;
    }

    // 메시지 비활성화
    public void deleteMessage() {
        this.active = false;
    }

}
