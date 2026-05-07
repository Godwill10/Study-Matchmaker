package com.studymatchmaker.controller;

import com.studymatchmaker.dto.MessageDto;
import com.studymatchmaker.dto.SendMessageRequest;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import com.studymatchmaker.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final UserRepository userRepository;

    @GetMapping("/unread-count")
    public long unreadCount(Principal principal) {
        return messageService.getUnreadCount(resolveUser(principal));
    }

    @GetMapping("/{friendId}")
    public List<MessageDto> conversation(Principal principal, @PathVariable Long friendId) {
        return messageService.getConversationAndMarkRead(resolveUser(principal), friendId);
    }

    @PostMapping
    public MessageDto send(Principal principal, @Valid @RequestBody SendMessageRequest request) {
        return messageService.send(resolveUser(principal), request);
    }

    private User resolveUser(Principal principal) {
        return userRepository.findByEmail(principal.getName().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}