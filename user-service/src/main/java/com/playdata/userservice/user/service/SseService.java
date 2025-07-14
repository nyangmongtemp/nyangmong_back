package com.playdata.userservice.user.service;

import com.playdata.userservice.user.controller.SseController;
import com.playdata.userservice.user.dto.noti.MessageNotiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SseService {

    private final SseController sseController;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "user.message.notifications")
    public void onMessageReceived(MessageNotiDto message) {
        Long receiverId = message.getReceiverId();

        if (sseController.isConnected(receiverId)) {
            sseController.getEmitter(receiverId).ifPresent(emitter -> {
                try {
                    emitter.send(SseEmitter.event()
                            .name("message")
                            .data(message));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            });
        } else {
            // 미접속자 → pending 큐에 넣기
            rabbitTemplate.convertAndSend("", "user.pending.notifications", message);
        }

    }

}
