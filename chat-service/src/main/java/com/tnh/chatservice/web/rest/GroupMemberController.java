package com.tnh.chatservice.web.rest;

import com.tnh.chatservice.dto.ChatProfileDTO;
import com.tnh.chatservice.dto.GroupMemberDTO;
import com.tnh.chatservice.mapper.GroupMemberMapper;
import com.tnh.chatservice.service.GroupMemberService;
import com.tnh.chatservice.utils.SecurityUtils;
import com.tnh.chatservice.utils.exception.InvalidDataException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/group-member")
public class GroupMemberController {

    private final GroupMemberService groupMemberService;

    private final GroupMemberMapper groupMemberMapper;

    public GroupMemberController(GroupMemberService groupMemberService,
                                 GroupMemberMapper groupMemberMapper) {
        this.groupMemberService = groupMemberService;
        this.groupMemberMapper = groupMemberMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<GroupMemberDTO>> getGroupsMembersById(@PathVariable("id") Long id) {
        return ResponseEntity.ok()
                .body(groupMemberMapper.mapToGroupMemberList(groupMemberService.getAllGroupsMembersByGroup(id)));
    }

    @PostMapping
    public ResponseEntity<List<GroupMemberDTO>> addMember(@RequestBody List<GroupMemberDTO> members) {
        var id = members.get(0).getGroup().getId();
        if (groupMemberService.isAdmin(id, SecurityUtils.getCurrentUser())) {
            for (var mem: members) {
                groupMemberService.createGroupMember(id, mem.getMember().getUserId(), mem.isAdmin());
            }
            return ResponseEntity.ok()
                    .body(groupMemberMapper.mapToGroupMemberList(groupMemberService.getAllGroupsMembersByGroup(id)));
        }

        throw new InvalidDataException("You're not admin");

    }


}
