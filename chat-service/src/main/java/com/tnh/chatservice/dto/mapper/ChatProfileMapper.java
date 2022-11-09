package com.tnh.chatservice.dto.mapper;

import com.tnh.chatservice.domain.ChatProfile;
import com.tnh.chatservice.dto.ChatProfileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ChatProfileMapper {

    @Named("chatProfileToChatProfileDTO")
    @Mapping(target = "userId", expression = "java(convertUUIDtoString(chatProfile.getUserId()))")
    ChatProfileDTO chatProfileToChatProfileDTO(ChatProfile chatProfile);

    default String convertUUIDtoString(UUID id) {
        return id.toString();
    }

}
