package com.tnh.chatservice.messaging.listener;

import com.tnh.chatservice.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import com.tnh.chatservice.service.ChatProfileService;

@Slf4j
@Component
public class RabbitNewUserListener {

    private final ChatProfileService chatProfileService;

    public RabbitNewUserListener(ChatProfileService chatProfileService) {
        this.chatProfileService = chatProfileService;
    }


    @RabbitListener(queues = "#{newUsersQueue.name}", messageConverter = "Jackson2JsonMessageConverter")
    public void receiveNewUser(UserDTO userDTO) {
        log.debug("New user {}", userDTO.getUsername());
        chatProfileService.createChatProfile(userDTO.getId(), userDTO.getUsername());
    }


}
