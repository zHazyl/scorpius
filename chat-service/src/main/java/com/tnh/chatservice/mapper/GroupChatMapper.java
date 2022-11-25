package com.tnh.chatservice.mapper;

import com.tnh.chatservice.domain.GroupChat;
import com.tnh.chatservice.dto.GroupChatDTO;
import com.tnh.chatservice.utils.DateUtils;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupChatMapper {

    @Named("mapToGroupChatDTO")
    @Mapping(target = "createdDate", expression = "java(convertOffsetDateToString(groupChat.getCreatedDate()))")
    GroupChatDTO mapToGroupChatDTO(GroupChat groupChat);

    @IterableMapping(qualifiedByName = {"mapToGroupChatDTO"})
    List<GroupChatDTO> mapToGroupChatList(List<GroupChat> groupChats);

    default String convertOffsetDateToString(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN));
    }
}
