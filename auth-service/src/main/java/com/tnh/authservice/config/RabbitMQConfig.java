package com.tnh.authservice.config;

import com.tnh.authservice.messaging.sender.UserSender;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    //type of exchange
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("com.tnh.authservice.fanout");
    }

    @Bean
    public UserSender userSender(RabbitTemplate rabbitTemplate, FanoutExchange fanoutExchange) {
        return new UserSender(rabbitTemplate, fanoutExchange);
    }
}
