//package com.playdata.userservice.common.configs;
//
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RabbitMqConfig {
//
//    @Bean
//    public TopicExchange messageExchange() {
//
//        return new TopicExchange("message.exchange");
//
//    }
//
//    @Bean
//    public Queue messageQueue() {
//
//        return QueueBuilder.durable("user.message.notifications")
//                .withArgument("x-message=ttl", 3600000)
//                .build();
//    }
//
//    // 대기 알림용 큐 (알림 전송 대상 관리자가 없을 시 메시지 누적)
//    @Bean
//    public Queue pendingNotificationQueue() {
//
//        return QueueBuilder.durable("user.pending.notifications")
//                .withArgument("x-message-ttl", 86400000)   // 1000 * 60 * 60 * 24
//                .build();
//
//    }
//
//    @Bean
//    public Binding userNotificationBinding() {
//
//        /**
//         * Exchange와 Queue를 연결하는 규칙
//         * "order.created" 패턴의 메시지가 오면 → admin.order.notifications 큐로 보내라!
//         */
//
//        return BindingBuilder
//                .bind(messageQueue())
//                .to(messageExchange())
//                .with("message.create");
//
//    }
//
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//
//        /**
//         * 우리가 보낼 객체 (OrderNotificationEvent -> DTO)를 JSON으로 변환
//         * 받을 때도 JSON을 다시 객체로 변환
//         */
//
//        return new Jackson2JsonMessageConverter();
//    }
//
//    // RabbitMQ를 사용하는 핵심 객체
//    @Bean
//    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//
//        // RabbitTemplate 설정
//        // 역할: 메시지 발송자 (Producer) - "편지를 우체통에 넣는 도구"
//
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
//        rabbitTemplate.setMessageConverter(jsonMessageConverter());
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
//
//        // Listener 설정
//        // 역할: 메시지 수신자 (Consumer) - "사서함에서 편지를 자동으로 꺼내주는 도구"
//
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setMessageConverter(jsonMessageConverter());
//        return factory;
//    }
//
//}
