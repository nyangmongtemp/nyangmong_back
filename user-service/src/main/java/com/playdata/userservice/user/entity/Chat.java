package com.playdata.userservice.user.entity;

import com.playdata.userservice.common.entity.BaseTimeEntity;
import com.playdata.userservice.user.dto.chat.res.UserChatInfoResDto;
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
@Table(name = "tbl_chat")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatId;

    private Long userId1;

    private Long userId2;

    private boolean active;

    @OneToMany(mappedBy = "chat")
    private List<Message> messageList;

    // 채팅방 생성 메소드


    public Chat(Long userId2, Long userId1) {
        this.userId2 = userId2;
        this.userId1 = userId1;
        this.active = true;
    }

    // 채팅방 삭제 메소드
    public void deleteChat() {
        this.active = false;
        // 채팅방의 모든 메시지도 비활성화
        messageList.stream().forEach(Message::deleteMessage);
    }

    // 채팅방 정보 dto 변환 메소드
    public UserChatInfoResDto toUserChatInfoResDto(String n1, String n2
            , String requestNickname, UserMessageResDto resDto) {
        return UserChatInfoResDto.builder()
                .chatId(chatId)
                .userId1(userId1)
                .userId2(userId2)
                .createAt(this.getCreateAt())
                .updateAt(this.getUpdateAt())
                .nickname1(n1)
                .nickname2(n2)
                .requestNickname(requestNickname)
                .message(resDto)
                .build();
    }

}
