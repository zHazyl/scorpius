package com.tnh.chatservice.config;

import com.tnh.chatservice.messaging.sender.DeleteMessagesSender;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("com.tnh.authservice.fanout");
    }

    @Bean
    public Queue newUsersQueue() {
        return new Queue("com.tnh.chatservice.account");
    }

    @Bean
    public Binding newUser(FanoutExchange fanoutExchange, Queue newUsersQueue) {
        return BindingBuilder.bind(newUsersQueue).to(fanoutExchange);
    }

    @Bean
    public FanoutExchange deletingMessageExchange() {
        return new FanoutExchange("com.tnh.chatmessagesservice.fanout.deleting");
    }

    @Bean
    public DeleteMessagesSender deleteMessagesSender(RabbitTemplate rabbitTemplate, FanoutExchange deletingMessageExchange) {
        return new DeleteMessagesSender(rabbitTemplate, deletingMessageExchange);
    }

    @Bean("Jackson2JsonMessageConverter")
    Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }


}
