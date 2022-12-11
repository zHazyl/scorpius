package com.tnh.chatservice.web.rest;

import com.tnh.chatservice.dto.GroupChatDTO;
import com.tnh.chatservice.mapper.GroupChatMapper;
import com.tnh.chatservice.service.GroupChatService;
import com.tnh.chatservice.service.GroupMemberService;
import com.tnh.chatservice.utils.SecurityUtils;
import com.tnh.chatservice.utils.exception.InvalidDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group-chats")
public class GroupChatController {
    private final GroupChatService groupChatService;

    private final GroupMemberService groupMemberService;

    private final GroupChatMapper groupChatMapper;

    public GroupChatController(GroupChatService groupChatService,
                               GroupMemberService groupMemberService,
                               GroupChatMapper groupChatMapper) {
        this.groupChatService = groupChatService;
        this.groupMemberService = groupMemberService;
        this.groupChatMapper = groupChatMapper;
    }

    @GetMapping
    public ResponseEntity<List<GroupChatDTO>> getAllGroupsChats() {

        var allGroupsChatsByMember = groupChatService.getAllGroupsChatsByMember(SecurityUtils.getCurrentUser());

        return ResponseEntity.ok()
                .body(groupChatMapper.mapToGroupChatList(allGroupsChatsByMember));
    }

    @PostMapping
    public GroupChatDTO createGroupChat(@RequestBody GroupChatDTO groupChatDTO) {
        var group = groupChatService.createGroupChat(groupChatDTO.getGroupName());
        groupMemberService.createGroupMember(group.getId(), SecurityUtils.getCurrentUser(), true);
        return groupChatMapper.mapToGroupChatDTO(group);
    }

    @DeleteMapping(params = {"group_id"})
    public ResponseEntity<Void> deleteGroupChat(@RequestParam("group_id") long groupId) {
        if (this.groupMemberService.isAdmin(groupId, SecurityUtils.getCurrentUser())) {
            this.groupMemberService.deleteMemberByGroup(groupId);
            this.groupChatService.deleteGroupChat(groupId);
        } else {
            throw new InvalidDataException("You're not admin");
        }
        return ResponseEntity.noContent().build();
    }

}
