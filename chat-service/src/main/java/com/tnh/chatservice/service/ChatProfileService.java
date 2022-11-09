package com.tnh.chatservice.service;

import com.tnh.chatservice.domain.ChatProfile;

public interface ChatProfileService {
    ChatProfile createChatProfile(String userId, String username);

    ChatProfile generateNewFriendsRequestCode(String userId, String username);

    ChatProfile getChatProfileById(String userId);
}
