package com.studymatchmaker.service;

import com.studymatchmaker.dto.MessageDto;
import com.studymatchmaker.dto.SendMessageRequest;
import com.studymatchmaker.exception.BadRequestException;
import com.studymatchmaker.exception.NotFoundException;
import com.studymatchmaker.model.Message;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.FriendshipRepository;
import com.studymatchmaker.repository.MessageRepository;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final MapperService mapperService;

    @Transactional
    public List<MessageDto> getConversationAndMarkRead(User currentUser, Long friendId) {
        if (!friendshipRepository.areFriendsByIds(currentUser.getId(), friendId)) {
            throw new BadRequestException("You can only message your friends");
        }

        List<MessageDto> messages = messageRepository.findConversation(currentUser.getId(), friendId).stream()
                .map(this::toDto)
                .toList();

        messageRepository.markConversationAsRead(currentUser.getId(), friendId);

        return messages;
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(User currentUser) {
        return messageRepository.countByReceiverIdAndReadAtIsNull(currentUser.getId());
    }

    @Transactional
    public MessageDto send(User sender, SendMessageRequest request) {
        Long receiverId = request.getReceiverId();

        if (sender.getId().equals(receiverId)) {
            throw new BadRequestException("You cannot message yourself");
        }

        if (!friendshipRepository.areFriendsByIds(sender.getId(), receiverId)) {
            throw new BadRequestException("You can only message your friends");
        }

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new NotFoundException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(request.getContent().trim())
                .build();

        return toDto(messageRepository.save(message));
    }

    private MessageDto toDto(Message message) {
        return MessageDto.builder()
                .id(message.getId())
                .sender(mapperService.toUserProfile(message.getSender()))
                .receiver(mapperService.toUserProfile(message.getReceiver()))
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .build();
    }
}