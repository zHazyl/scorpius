package com.tnh.chatmessagesservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    private static final String MESSAGE_STORING_QUEUE = "com.tnh.chatmessagesservice.storing";
    private static final String MESSAGE_STORING_EXCHANGE = "com.tnh.chatmessagesservice.fanout";
    private static final String MESSAGE_STORING_DLQ = MESSAGE_STORING_QUEUE + ".dlq"; // dead letter queue
    private static final String MESSAGE_STORING_DLE = MESSAGE_STORING_QUEUE + ".dlx"; // dead letter exchange
    private static final String MESSAGE_DELETING_QUEUE = "com.tnh.chatmessagesservice.deleting";
    private static final String MESSAGE_DELETING_EXCHANGE = "com.tnh.chatmessagesservice.fanout.deleting";

    @Bean
    public FanoutExchange messageStoringExchange() {
        return new FanoutExchange(MESSAGE_STORING_EXCHANGE);
    }

    @Bean
    public Queue messageStoringQueue() {
        return QueueBuilder.durable(MESSAGE_STORING_QUEUE)
                .deadLetterExchange(MESSAGE_STORING_DLE)
                .build();
    }

    @Bean
    public Binding messageStoringBinding(Queue messageStoringQueue, FanoutExchange messageStoringExchange) {
        return BindingBuilder.bind(messageStoringQueue).to(messageStoringExchange);
    }

    @Bean
    public FanoutExchange messageDeletingExchange() {
        return new FanoutExchange(MESSAGE_DELETING_EXCHANGE);
    }

    @Bean
    public Queue messageDeletingQueue() {
        return QueueBuilder.durable(MESSAGE_DELETING_QUEUE).build();
    }

    @Bean
    public Binding messageDeletingBinding(Queue messageDeletingQueue, FanoutExchange messageDeletingExchange) {
        return BindingBuilder.bind(messageDeletingQueue).to(messageDeletingExchange);
    }

    @Bean
    public FanoutExchange deadLetterExchange() {
        return new FanoutExchange(MESSAGE_STORING_DLE);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(MESSAGE_STORING_DLQ)
                .ttl(5000)
                .build();
    }

    @Bean
    public Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
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
