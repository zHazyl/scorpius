package com.tnh.chatservice.service;

import com.tnh.chatservice.domain.GroupChat;

import java.util.List;

public interface GroupChatService {
    GroupChat createGroupChat(String name);

    GroupChat getGroupChatById(Long id);

    List<GroupChat> getAllGroupsChatsByMember(String currentUser);
}
