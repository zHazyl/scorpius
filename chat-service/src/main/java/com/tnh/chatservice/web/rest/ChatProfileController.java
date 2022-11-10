package com.tnh.chatservice.web.rest;

import com.tnh.chatservice.dto.ChatProfileDTO;
import com.tnh.chatservice.service.ChatProfileService;
import com.tnh.chatservice.utils.SecurityUtils;
import com.tnh.chatservice.utils.exception.InvalidDataException;
//import com.tnh.chatservice.utils.security.SecurityUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.tnh.chatservice.mapper.ChatProfileMapper;

@RestController
@RequestMapping("/chat-profiles")
public class ChatProfileController {

    private final ChatProfileService chatProfileService;
    private final ChatProfileMapper chatProfileMapper;

    public ChatProfileController(ChatProfileService chatProfileService,
                                 ChatProfileMapper chatProfileMapper) {
        this.chatProfileService = chatProfileService;
        this.chatProfileMapper = chatProfileMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatProfileDTO> getChatProfileById(@PathVariable("id") String id) {
        return ResponseEntity.ok()
                .body(chatProfileMapper.chatProfileToChatProfileDTO(chatProfileService.getChatProfileById(id)));
    }

//    @PatchMapping("/{id}/new-friends-request-code")
//    public ResponseEntity<ChatProfileDTO> generateNewFriendsRequestCode(@PathVariable("id") String userId) {
//        if (!userId.equals(SecurityUtils.getCurrentUser())) {
//            throw new InvalidDataException("Invalid user id");
//        }
//        var chatProfile = chatProfileService.generateNewFriendsRequestCode(userId, SecurityUtils.getCurrentUserPreferredUsername());
//        return ResponseEntity.ok()
//                .body(chatProfileMapper.chatProfileToChatProfileDTO(chatProfile));
//    }


}
