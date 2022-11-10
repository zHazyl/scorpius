package com.tnh.chatmessagesservice.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import com.tnh.chatmessagesservice.constant.MessageStatus;
import com.tnh.chatmessagesservice.document.ChatMessage;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Date;
import java.util.List;


public interface ChatMessageRepository extends ReactiveCrudRepository<ChatMessage, String> {

    Flux<ChatMessage> findByTimeLessThanAndFriendChatIn(Date time, Collection<Long> ids, Pageable pageable);

    Flux<ChatMessage> findByFriendChatOrFriendChat(long firstUserChatId, long secondUserChatId, Pageable pageable);

    Flux<ChatMessage> findByFriendChatAndRecipientAndStatus(long friendChatId, String userId, MessageStatus status);

    Mono<Void> deleteByFriendChatIn(List<Long> ids);
}
