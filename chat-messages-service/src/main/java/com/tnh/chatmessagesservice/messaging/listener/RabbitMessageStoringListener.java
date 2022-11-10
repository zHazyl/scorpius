package com.tnh.chatmessagesservice.messaging.listener;

import com.tnh.chatmessagesservice.dto.ChatMessageDTO;
import com.tnh.chatmessagesservice.service.ChatMessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMessageStoringListener {

    private final ChatMessageService chatMessageService;

    public RabbitMessageStoringListener(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @RabbitListener(queues = "#{messageStoringQueue.name}")
    public void receiveNewChatMessage(ChatMessageDTO chatMessageDTO) {
        chatMessageService.saveChatMessage(chatMessageDTO.getFriendChat(),
                chatMessageDTO.getSender(),
                chatMessageDTO.getRecipient(),
                chatMessageDTO.getContent(),
                chatMessageDTO.getTime()).subscribe();
    }

}
