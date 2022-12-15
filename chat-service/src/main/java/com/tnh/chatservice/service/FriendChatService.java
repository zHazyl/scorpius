package com.tnh.chatservice.service;

import com.tnh.chatservice.domain.ChatProfile;
import com.tnh.chatservice.domain.FriendChat;
import com.tnh.chatservice.domain.FriendChatRedis;

import java.util.List;

public interface FriendChatService {
    void createFriendChat(ChatProfile firstUserChatProfile, ChatProfile secondUserChatProfile);

    List<FriendChat> getAllFriendsChatsBySender(String currentUser);

    void deleteFriendChat(long friendChatId, long friendChatWithId, String currentUserId);

    List<FriendChatRedis> getAllFriendChatsRedisBySender(String currentUser);
}
