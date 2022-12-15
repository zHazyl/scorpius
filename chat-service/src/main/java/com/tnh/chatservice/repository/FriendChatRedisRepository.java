package com.tnh.chatservice.repository;

import com.tnh.chatservice.domain.ChatProfile;
import com.tnh.chatservice.domain.FriendChat;
import com.tnh.chatservice.domain.FriendChatRedis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FriendChatRedisRepository {

    private final RedisTemplate template;

    public FriendChatRedisRepository(RedisTemplate template) {
        this.template = template;
    }

    public FriendChatRedis save(FriendChat friendChat) {
        FriendChatRedis friendChatRedis = new FriendChatRedis(
                friendChat.getId(),
                friendChat.getChatWith().getId(),
                friendChat.getSender().getUserId().toString(),
                friendChat.getRecipient().getUserId().toString()
        );
        template.opsForHash().put(friendChatRedis.getSender(), friendChatRedis.getId().toString(), friendChatRedis);
        return friendChatRedis;
    }

    public void deleteFriendChat(String sender) {
        template.opsForHash().delete(sender);
    }

    public List<FriendChatRedis> findAllBySender(String sender) {
        List<FriendChatRedis> friendChatRedisList =
                template.opsForHash().values(sender);
        return friendChatRedisList;
    }

//    public List<FriendChat> findAllBySender(String sender) {
//        List<FriendChat> friendChats = new ArrayList<>();
//        var listFriendChatRedis = (List<FriendChatRedis>) template.opsForHash().values(sender);
//        listFriendChatRedis.forEach(friendChatRedis -> {
//            FriendChat friendChat = new FriendChat();
//            friendChat.setId(Long.valueOf(friendChatRedis.getId()));
//            ChatProfile senderInfo = new ChatProfile();
//            senderInfo.setUserId(UUID.fromString(friendChatRedis.getSender()));
//            friendChat.setSender(
//                    senderInfo
//            );
//            ChatProfile recipient = new ChatProfile();
//            recipient.setUserId(UUID.fromString(friendChatRedis.getRecipient()));
//            friendChat.setRecipient(recipient);
//            FriendChat chatWith = new FriendChat();
//            chatWith.setId(Long.valueOf(friendChatRedis.getChatWith()));
//            chatWith.setSender(recipient);
//            chatWith.setRecipient(senderInfo);
//            friendChat.setChatWith(chatWith);
//            friendChats.add(friendChat);
//        });
//
//        return friendChats;
//
//    }
//
//    public void deleteFriendChat(FriendChat friendChat) {
//        template.opsForHash().delete(friendChat.getSender().getUserId().toString());
//    }
//
//    public FriendChatRedis save(FriendChat friendChat) {
//        FriendChatRedis friendChatRedis = new FriendChatRedis();
//        friendChatRedis.setChatWith(friendChat.getChatWith().toString());
//        friendChatRedis.setId(friendChat.getId().toString());
//        friendChatRedis.setSender(friendChat.getSender().getUserId().toString());
//        friendChatRedis.setRecipient(friendChat.getRecipient().getUserId().toString());
//        template.opsForHash().put(
//                friendChatRedis.getSender(),
//                friendChatRedis.getId(),
//                friendChatRedis
//        );
//        return friendChatRedis;
//    }

}
