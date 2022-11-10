package com.tnh.chatmessagesservice.messaging.listener;

import com.tnh.chatmessagesservice.service.ChatMessageService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitMessageDeletingListener {

    private final ChatMessageService chatMessageService;

    public RabbitMessageDeletingListener(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @RabbitListener(queues = "#{messageDeletingQueue.name}")
    public void receiveNewChatMessage(List<Long> friendChatIds) {
        chatMessageService.removeMessagesByFriendChat(friendChatIds).subscribe();
    }

}
