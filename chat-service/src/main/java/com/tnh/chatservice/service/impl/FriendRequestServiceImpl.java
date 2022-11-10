package com.tnh.chatservice.service.impl;

import com.tnh.chatservice.repository.ChatProfileRepository;
import com.tnh.chatservice.repository.FriendRequestRepository;
import com.tnh.chatservice.service.FriendChatService;
import com.tnh.chatservice.utils.exception.AlreadyExistsException;
import com.tnh.chatservice.utils.exception.InvalidDataException;
import com.tnh.chatservice.utils.exception.NotFoundException;
import org.springframework.stereotype.Service;
import com.tnh.chatservice.domain.FriendRequest;
import com.tnh.chatservice.service.FriendRequestService;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FriendRequestServiceImpl implements FriendRequestService {

    private final ChatProfileRepository chatProfileRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendChatService friendChatService;

    public FriendRequestServiceImpl(ChatProfileRepository chatProfileRepository,
                                    FriendRequestRepository friendRequestRepository,
                                    FriendChatService friendChatService) {
        this.chatProfileRepository = chatProfileRepository;
        this.friendRequestRepository = friendRequestRepository;
        this.friendChatService = friendChatService;
    }

    @Override
    public FriendRequest createNewFriendRequest(String senderUserId, String friendRequestCode) {

        var senderChatProfile = chatProfileRepository.findById(UUID.fromString(senderUserId))
                .orElseThrow(() -> new NotFoundException("Chat profile not found."));

        var recipientChatProfile = chatProfileRepository.findByFriendsRequestCode(friendRequestCode)
                .orElseThrow(() -> new NotFoundException("Friend request code not found"));

        if (senderChatProfile.getFriendsRequestCode().equals(friendRequestCode))
            throw new InvalidDataException("You can't send friend request to yourself.");

        if (friendRequestRepository.isFriendRequestAlreadyExists(senderChatProfile, recipientChatProfile)) {
            throw new AlreadyExistsException("Friend request already registered.");
        }

        var newFriendsRequest = new FriendRequest();
        newFriendsRequest.setSender(senderChatProfile);
        newFriendsRequest.setRecipient(recipientChatProfile);
        newFriendsRequest.setSentTime(OffsetDateTime.now());
        return friendRequestRepository.save(newFriendsRequest);
    }

    @Override
    public void replyToFriendRequest(long friendId, String currentUserId, boolean accept) {

        var friendsRequest = friendRequestRepository.findById(friendId)
                .orElseThrow(() -> new NotFoundException("Friend request not found"));

        if (friendsRequest.isAccepted()) {
            throw new InvalidDataException("Friend request already accepted.");
        }

        if (!friendsRequest.getRecipient().getUserId().equals(UUID.fromString(currentUserId))) {
            throw new InvalidDataException("You are not recipient of this request request");
        }

        if (accept) {
            friendsRequest.setAccepted(true);
            friendRequestRepository.save(friendsRequest);
            // create friend chat
            friendChatService.createFriendChat(friendsRequest.getSender(), friendsRequest.getRecipient());
        } else {
            friendRequestRepository.delete(friendsRequest);
        }
    }

    @Override
    public void deleteFriendRequestBySender(String currentUserId, long friendRequestId) {
        friendRequestRepository.findById(friendRequestId)
                .ifPresentOrElse(friendRequest -> {
                    if (!friendRequest.getSender().getUserId().equals(UUID.fromString(currentUserId))) {
                        throw new InvalidDataException("You can't cancel friend request because you aren't the owner");
                    }

                    if (friendRequest.isAccepted()) {
                        throw new InvalidDataException("Friend request already accepted.");
                    }
                    friendRequestRepository.delete(friendRequest);
                }, () -> {
                    throw new NotFoundException("Friend request not found");
                });
    }


    @Override
    public List<FriendRequest> getAllNotAcceptedFriendRequestsByRecipientId(String recipientId) {
        return friendRequestRepository.findAllByRecipientIdAndNotAccepted(UUID.fromString(recipientId));
    }

    @Override
    public List<FriendRequest> getAllNotAcceptedFriendRequestsBySenderId(String senderId) {
        return friendRequestRepository.findAllBySenderIdAndNotAccepted(UUID.fromString(senderId));
    }
}
