package com.tnh.chatservice.dto.mapper;

import com.tnh.chatservice.domain.FriendRequest;
import com.tnh.chatservice.dto.FriendRequestDTO;
import com.tnh.chatservice.util.DateUtils;
import org.mapstruct.*;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Mapper(componentModel = "spring", uses = ChatProfileMapper.class)
public interface FriendRequestMapper {

    @Mapping(target = "sender", qualifiedByName = "chatProfileToChatProfileDTO")
    @Mapping(target = "recipient", qualifiedByName = "chatProfileToChatProfileDTO")
    @Mapping(target = "sentTime", expression = "java(convertOffsetDateToString(friendRequest.getSentTime()))")
    FriendRequestDTO mapToFriendRequestDTO(FriendRequest friendRequest);

    @Named("mapWithoutRecipient")
    @InheritConfiguration(name = "mapToFriendRequestDTO")
    @Mapping(target = "recipient", ignore = true)
    FriendRequestDTO mapToFriendRequestDTOWithoutRecipient(FriendRequest friendRequest);

    @Named("mapWithoutSender")
    @InheritConfiguration(name = "mapToFriendRequestDTO")
    @Mapping(target = "sender", ignore = true)
    FriendRequestDTO mapToFriendRequestDTOWithoutSender(FriendRequest friendRequest);

    @IterableMapping(qualifiedByName = "mapWithoutRecipient")
    List<FriendRequestDTO> mapToFriendRequestDTOListWithoutRecipient(List<FriendRequest> friendRequests);

    @IterableMapping(qualifiedByName = "mapWithoutSender")
    List<FriendRequestDTO> mapToFriendRequestDTOListWithoutSender(List<FriendRequest> friendRequests);


    default String convertOffsetDateToString(OffsetDateTime time) {
        return time.format(DateTimeFormatter.ofPattern(DateUtils.DATE_PATTERN));
    }

}
