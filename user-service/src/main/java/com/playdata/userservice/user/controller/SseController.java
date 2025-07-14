package com.playdata.userservice.user.controller;

import com.playdata.userservice.common.auth.TokenUserInfo;
import com.playdata.userservice.common.enumeration.ErrorCode;
import com.playdata.userservice.common.exception.CommonException;
import com.playdata.userservice.user.dto.noti.MessageNotiDto;
import com.playdata.userservice.user.entity.User;
import com.playdata.userservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/user/sse")
@RequiredArgsConstructor
@Slf4j
public class SseController {

    // 사용자별 Emitter를 저장할 Map
    private final Map<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    private final RabbitTemplate rabbitTemplate;

    // 사용자 접속 시 Emitter 생성 및 저장
    @GetMapping("/subscribe")
    public SseEmitter subscribe(@AuthenticationPrincipal TokenUserInfo userInfo) {
        SseEmitter emitter = new SseEmitter(60 * 60 * 1000L); // 1시간 타임아웃

        long userId = userInfo.getUserId();

        sseEmitters.put(userId, emitter);

        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        emitter.onError(e -> sseEmitters.remove(userId));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("SSE 연결 성공"));

            // pendingQueue에 있는 메시지 꺼내기
            while (true) {
                Object obj = rabbitTemplate.receiveAndConvert("user.pending.notifications");
                if (obj == null) break;

                if (obj instanceof MessageNotiDto) {
                    MessageNotiDto message = (MessageNotiDto) obj;
                    if (Objects.equals(message.getReceiverId(), userId)) {
                        emitter.send(SseEmitter.event()
                                .name("message")
                                .data(message));
                    } else {
                        // 수신자가 다르면 다시 넣어줌 (보존)
                        rabbitTemplate.convertAndSend("", "user.pending.notifications", message);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            throw new CommonException(ErrorCode.INTERNAL_SERVER_ERROR, "sse 접속 중 장애 발생");
        }

        return emitter;
    }

    // 외부에서 emitter에 접근할 수 있도록 getter 제공
    public Optional<SseEmitter> getEmitter(Long userId) {
        return Optional.ofNullable(sseEmitters.get(userId));
    }

    // 접속 여부 확인 (외부 접근용)
    public boolean isConnected(Long userId) {
        return sseEmitters.containsKey(userId);
    }

}
