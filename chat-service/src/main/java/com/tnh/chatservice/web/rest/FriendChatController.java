package com.tnh.chatservice.web.rest;

import com.tnh.chatservice.domain.FriendChatRedis;
import com.tnh.chatservice.dto.ChatProfileDTO;
import com.tnh.chatservice.messaging.sender.DeleteMessagesSender;
import com.tnh.chatservice.service.FriendChatService;
import com.tnh.chatservice.utils.SecurityUtils;
import com.tnh.chatservice.utils.UserContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tnh.chatservice.dto.FriendChatDTO;
import com.tnh.chatservice.mapper.FriendChatMapper;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/friends-chats")
public class FriendChatController {


    private static final Logger logger = LoggerFactory.getLogger(FriendChatController.class);

    private final FriendChatService friendChatService;
    private final FriendChatMapper friendChatMapper;
    private final DeleteMessagesSender deleteMessagesSender;

    public FriendChatController(FriendChatService friendChatService,
                                FriendChatMapper friendChatMapper,
                                DeleteMessagesSender deleteMessagesSender) {
        this.friendChatService = friendChatService;
        this.friendChatMapper = friendChatMapper;
        this.deleteMessagesSender = deleteMessagesSender;
    }

    @GetMapping
    public ResponseEntity<List<FriendChatDTO>> getAllFriendsChats() {

        List<FriendChatRedis> friendChatRedisList = null;
        try {
            friendChatRedisList = friendChatService.getAllFriendChatsRedisBySender(
                    SecurityUtils.getCurrentUser());
            if (!friendChatRedisList.isEmpty()) {
                logger.debug("Redis");
                List<FriendChatDTO> friendChatDTOS = new ArrayList<>();
                friendChatRedisList.forEach(friendChatRedis -> {
                    FriendChatDTO friendChatDTO = new FriendChatDTO();
                    friendChatDTO.setId(friendChatRedis.getId());
                    friendChatDTO.setChatWith(friendChatRedis.getChatWith());
                    ChatProfileDTO chatProfileDTO = new ChatProfileDTO();
                    chatProfileDTO.setUserId(friendChatRedis.getRecipient());
                    friendChatDTO.setRecipient(chatProfileDTO);
                    friendChatDTOS.add(friendChatDTO);
                });
                return ResponseEntity.ok(friendChatDTOS);
            }
        } catch (Exception e) {
        }
        var allFriendsChatsBySender =
                friendChatService.getAllFriendsChatsBySender(
                SecurityUtils.getCurrentUser());
        return ResponseEntity.ok()
                .body(friendChatMapper.mapToFriendChatList(allFriendsChatsBySender));
    }

    @DeleteMapping(params = {"friend_chat", "friend_chat_with"})
    public ResponseEntity<Void> deleteUserFriendChat(@RequestParam("friend_chat") long friendChatId,
                                                     @RequestParam("friend_chat_with") long friendChatWithId) {
        friendChatService.deleteFriendChat(friendChatId, friendChatWithId, SecurityUtils.getCurrentUser());
        deleteMessagesSender.sendDeletingMessagesTask(List.of(friendChatId, friendChatWithId));
        return ResponseEntity.noContent().build();
    }


}
