package com.playdata.userservice.user.repository;

import com.playdata.userservice.user.entity.Chat;
import com.playdata.userservice.user.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MessageRepository extends JpaRepository<Message, Long> {

    
    // 특정 채팅방의 7일동안 활성화된 모든 메시지를 조회
    @Query("SELECT m FROM Message m WHERE m.active = true AND m.createAt >= :start" +
            " AND m.chat.chatId = :chatId")
    Optional<List<Message>> findByChatId(@Param("chatId") Long chatId,
                                         @Param("start") LocalDateTime start);

    // 채팅방 목록 조회 시, 가장 최근 메시지를 화면에 노출시키기 위한 조회
    @Query("SELECT m FROM Message m WHERE m.active = true AND m.chat.chatId = :chatId " +
            "ORDER BY m.createAt DESC LIMIT 1")
    Message findLastByChatId(@Param("chatId") Long chatId);


}
