package com.studymatchmaker.service;

import com.studymatchmaker.dto.ConnectionRequestDto;
import com.studymatchmaker.dto.FriendDto;
import com.studymatchmaker.exception.BadRequestException;
import com.studymatchmaker.exception.NotFoundException;
import com.studymatchmaker.model.ConnectionRequest;
import com.studymatchmaker.model.Friendship;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.ConnectionRequestRepository;
import com.studymatchmaker.repository.FriendshipRepository;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConnectionService {

    private final ConnectionRequestRepository requestRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final MapperService mapperService;

    @Transactional
    public ConnectionRequestDto sendRequest(User sender, Long receiverId) {
        if (sender.getId().equals(receiverId)) {
            throw new BadRequestException("Cannot send a connection request to yourself");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (friendshipRepository.areFriends(sender, receiver)) {
            throw new BadRequestException("You are already connected with this user");
        }

        requestRepository.findBySenderAndReceiver(sender, receiver).ifPresent(existing -> {
            if (existing.getStatus() == ConnectionRequest.Status.PENDING) {
                throw new BadRequestException("Connection request already sent");
            }
        });

        requestRepository.findBySenderAndReceiver(receiver, sender).ifPresent(existing -> {
            if (existing.getStatus() == ConnectionRequest.Status.PENDING) {
                throw new BadRequestException("This user has already sent you a request. Check your incoming requests.");
            }
        });

        ConnectionRequest request = ConnectionRequest.builder()
                .sender(sender)
                .receiver(receiver)
                .build();

        return toDto(requestRepository.save(request));
    }

    @Transactional
    public ConnectionRequestDto acceptRequest(User currentUser, Long requestId) {
        ConnectionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You can only accept requests sent to you");
        }

        if (request.getStatus() != ConnectionRequest.Status.PENDING) {
            throw new BadRequestException("This request has already been " + request.getStatus().name().toLowerCase());
        }

        request.setStatus(ConnectionRequest.Status.ACCEPTED);
        request.setRespondedAt(LocalDateTime.now());
        requestRepository.save(request);

        User userOne = request.getSender().getId() < request.getReceiver().getId()
                ? request.getSender() : request.getReceiver();
        User userTwo = request.getSender().getId() < request.getReceiver().getId()
                ? request.getReceiver() : request.getSender();

        if (!friendshipRepository.areFriends(userOne, userTwo)) {
            friendshipRepository.save(Friendship.builder()
                    .userOne(userOne)
                    .userTwo(userTwo)
                    .build());
        }

        return toDto(request);
    }

    @Transactional
    public ConnectionRequestDto declineRequest(User currentUser, Long requestId) {
        ConnectionRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request not found"));

        if (!request.getReceiver().getId().equals(currentUser.getId())) {
            throw new BadRequestException("You can only decline requests sent to you");
        }

        if (request.getStatus() != ConnectionRequest.Status.PENDING) {
            throw new BadRequestException("This request has already been " + request.getStatus().name().toLowerCase());
        }

        request.setStatus(ConnectionRequest.Status.DECLINED);
        request.setRespondedAt(LocalDateTime.now());
        return toDto(requestRepository.save(request));
    }

    public List<ConnectionRequestDto> getIncomingRequests(User user) {
        return requestRepository.findByReceiverAndStatus(user, ConnectionRequest.Status.PENDING)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<ConnectionRequestDto> getSentRequests(User user) {
        return requestRepository.findBySenderAndStatus(user, ConnectionRequest.Status.PENDING)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<FriendDto> getFriends(User currentUser) {
        Set<String> myCourses = currentUser.getCourses();

        return friendshipRepository.findAllByUser(currentUser).stream()
                .map(f -> {
                    User friend = f.getUserOne().getId().equals(currentUser.getId())
                            ? f.getUserTwo() : f.getUserOne();

                    List<String> shared = friend.getCourses().stream()
                            .filter(myCourses::contains)
                            .collect(Collectors.toList());

                    return FriendDto.builder()
                            .user(mapperService.toUserProfile(friend))
                            .connectedAt(f.getConnectedAt())
                            .sharedCourses(shared)
                            .build();
                })
                .toList();
    }

    public long getIncomingCount(User user) {
        return requestRepository.findByReceiverAndStatus(user, ConnectionRequest.Status.PENDING).size();
    }

    private ConnectionRequestDto toDto(ConnectionRequest r) {
        return ConnectionRequestDto.builder()
                .id(r.getId())
                .sender(mapperService.toUserProfile(r.getSender()))
                .receiver(mapperService.toUserProfile(r.getReceiver()))
                .status(r.getStatus().name())
                .createdAt(r.getCreatedAt())
                .respondedAt(r.getRespondedAt())
                .build();
    }
}
