package com.tnh.chatservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.tnh.chatservice.domain.ChatProfile;

import java.util.Optional;
import java.util.UUID;

public interface ChatProfileRepository extends JpaRepository<ChatProfile, UUID> {

    Optional<ChatProfile> findByFriendsRequestCode(String friendsRequestCode);

}
