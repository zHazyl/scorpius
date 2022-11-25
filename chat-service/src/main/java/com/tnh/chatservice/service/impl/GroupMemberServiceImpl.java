package com.tnh.chatservice.service.impl;

import com.tnh.chatservice.domain.ChatProfile;
import com.tnh.chatservice.domain.GroupMember;
import com.tnh.chatservice.repository.GroupChatRepository;
import com.tnh.chatservice.repository.GroupMemberRepository;
import com.tnh.chatservice.service.ChatProfileService;
import com.tnh.chatservice.service.GroupChatService;
import com.tnh.chatservice.service.GroupMemberService;
import com.tnh.chatservice.utils.exception.AlreadyExistsException;
import com.tnh.chatservice.utils.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {

    private final GroupMemberRepository groupMemberRepository;

    private final GroupChatRepository groupChatRepository;

    private final ChatProfileService chatProfileService;


    public GroupMemberServiceImpl(GroupMemberRepository groupMemberRepository,
                                  GroupChatRepository groupChatRepository,
                                  ChatProfileService chatProfileService) {
        this.groupMemberRepository = groupMemberRepository;
        this.groupChatRepository = groupChatRepository;
        this.chatProfileService = chatProfileService;
    }

    @Override
    public void createGroupMember(Long group, String member, boolean isAdmin) {
        ChatProfile memberProfile = null;

        try {
            memberProfile = chatProfileService.getChatProfileById(member);
        } catch (Exception e) {
            throw new NotFoundException("Chat profile not found.");
        }
        var groupMember = new GroupMember();
        groupMember.setGroup(groupChatRepository.getGroupChatById(group));

        if (groupMemberRepository.existsGroupMemberByGroupAndMember(groupMember.getGroup(), groupMember.getMember())) {
            throw new AlreadyExistsException("This person has already been member");
        }

        groupMember.setMember(memberProfile);
        groupMember.setAdmin(isAdmin);

        groupMemberRepository.save(groupMember);

    }

    @Override
    public List<GroupMember> getAllGroupsMembersByGroup(Long group) {
        try{
            return this.groupMemberRepository.findByGroup(groupChatRepository.getGroupChatById(group));
        } catch (Exception e) {
            throw new NotFoundException("This group is not found");
        }
    }

    @Override
    public List<GroupMember> getAllGroupsMembersByMember(String currentUser) {
        try{
            return this.groupMemberRepository.findByMember(chatProfileService.getChatProfileById(currentUser));
        } catch (Exception e) {
            throw new NotFoundException("This user " + currentUser + " is not found");
        }
    }

    @Override
    public boolean isAdmin(Long group, String member) {
        var mem = groupMemberRepository.findGroupMemberByGroupAndMember(
                groupChatRepository.getGroupChatById(group),
                chatProfileService.getChatProfileById(member));
        return mem.isAdmin();
    }
}
