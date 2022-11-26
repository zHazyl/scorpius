package com.tnh.chatmessagesservice.web.rest;

import com.tnh.chatmessagesservice.service.ChatMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.tnh.chatmessagesservice.document.ChatMessage;
import com.tnh.chatmessagesservice.security.SecurityUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping("/chat-messages")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    public ChatMessageController(ChatMessageService chatMessageService) {
        this.chatMessageService = chatMessageService;
    }

    @GetMapping
    public Flux<ChatMessage> getLastUsersMessagesFromTimeWithSize(@RequestParam("friend_chat_id1") long friendChatId1,
                                                                  @RequestParam("friend_chat_id2") long friendChatId2,
                                                                  @RequestParam(value = "from", required = false) String fromTime,
                                                                  @RequestParam("size") int numberOfMessagesToFetch) {
        if (fromTime != null) {
            return chatMessageService.findLastUsersMessagesFromTime(friendChatId1, friendChatId2, fromTime, numberOfMessagesToFetch);
        } else {
            return chatMessageService.getLastUserMessages(friendChatId1, friendChatId2, numberOfMessagesToFetch);
        }
    }

    @GetMapping("/group")
    public Flux<ChatMessage> getLastGroupMessagesFromTimeWithSize(@RequestParam("group_id") long group_id,
                                                                 @RequestParam(value = "from", required = false) String fromTime,
                                                                 @RequestParam("size") int numberOfMessagesToFetch) {
        if (fromTime != null) {
            return chatMessageService.findLastGroupMessagesFromTime(group_id, fromTime, numberOfMessagesToFetch);
        } else {
            return chatMessageService.getLastGroupMessages(group_id, numberOfMessagesToFetch);
        }
    }

    @PatchMapping(params = "friend_chat_id")
    public Mono<Void> setDeliveredStatusForAllRecipientMessagesInFriendChat(@RequestParam("friend_chat_id") long friendChatId) {
        return SecurityUtils.getCurrentUser()
                .flatMap(currentUser -> chatMessageService.setDeliveredStatusForAllRecipientMessagesInFriendChat(friendChatId, currentUser));
    }

}
