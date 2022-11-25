package com.tnh.chatservice.mapper;

import com.tnh.chatservice.domain.GroupMember;
import com.tnh.chatservice.dto.GroupMemberDTO;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ChatProfileMapper.class, GroupChatMapper.class})
public interface GroupMemberMapper {

    @Named("mapToGroupMemberDTO")
    @Mapping(target = "group", qualifiedByName = {"mapToGroupChatDTO"})
    @Mapping(target = "member", qualifiedByName = {"chatProfileToChatProfileDTO"})
    GroupMemberDTO mapToGroupMemberDTO(GroupMember groupMember);

    @IterableMapping(qualifiedByName = {"mapToGroupMemberDTO"})
    List<GroupMemberDTO> mapToGroupMemberList(List<GroupMember> groupMembers);
}
