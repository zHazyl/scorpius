package com.tnh.messageswebsocketservice.config;

import com.tnh.messageswebsocketservice.messaging.sender.StoringMessagesSender;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("com.tnh.chatmessagesservice.fanout");
    }

    @Bean
    public StoringMessagesSender storingMessagesSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        return new StoringMessagesSender(rabbitTemplate, fanoutExchange);
    }
}
