package com.tnh.messageswebsocketservice.messaging.sender;

import com.tnh.messageswebsocketservice.dto.ChatMessageDTO;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class StoringMessagesSender {

    private final RabbitTemplate template;
    private final FanoutExchange fanout;

    public StoringMessagesSender(RabbitTemplate template, FanoutExchange fanout) {
        this.template = template;
        this.fanout = fanout;
    }

    public void send(ChatMessageDTO chatMessageDTO){
        var converter = template.getMessageConverter();
        var messageProperties = new MessageProperties();
        var message = converter.toMessage(chatMessageDTO, messageProperties);
        template.send(fanout.getName(), "", message);
    }


}
