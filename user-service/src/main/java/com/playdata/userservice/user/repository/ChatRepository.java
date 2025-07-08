package com.playdata.userservice.user.repository;

import com.playdata.userservice.user.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    // 사용자 간의 기존에 생성된 채팅방 조회
    @Query("SELECT c FROM Chat c WHERE (c.userId1 = :userId1 AND c.userId2 = :userId2) OR " +
            "(c.userId1 = :userId2 AND c.userId2 = :userId1) AND c.active = true")
    Optional<Chat> findByUserId(Long userId1, Long userId2);

    // 사용자의 활성화된 모든 채팅방 조회
    @Query("SELECT c FROM Chat c WHERE c.active = true AND (c.userId1 = :userId OR c.userId2 = :userId)")
    Optional<List<Chat>> findMyActiveChat(Long userId);

}
