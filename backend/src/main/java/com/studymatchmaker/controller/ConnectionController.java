package com.studymatchmaker.controller;

import com.studymatchmaker.dto.ConnectionRequestDto;
import com.studymatchmaker.dto.FriendDto;
import com.studymatchmaker.dto.SendConnectionRequest;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import com.studymatchmaker.service.ConnectionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/connections")
@RequiredArgsConstructor
public class ConnectionController {

    private final ConnectionService connectionService;
    private final UserRepository userRepository;

    @PostMapping("/request")
    public ConnectionRequestDto sendRequest(Principal principal, @Valid @RequestBody SendConnectionRequest body) {
        return connectionService.sendRequest(resolveUser(principal), body.getReceiverId());
    }

    @PostMapping("/request/{id}/accept")
    public ConnectionRequestDto accept(Principal principal, @PathVariable Long id) {
        return connectionService.acceptRequest(resolveUser(principal), id);
    }

    @PostMapping("/request/{id}/decline")
    public ConnectionRequestDto decline(Principal principal, @PathVariable Long id) {
        return connectionService.declineRequest(resolveUser(principal), id);
    }

    @GetMapping("/requests/incoming")
    public List<ConnectionRequestDto> incoming(Principal principal) {
        return connectionService.getIncomingRequests(resolveUser(principal));
    }

    @GetMapping("/requests/sent")
    public List<ConnectionRequestDto> sent(Principal principal) {
        return connectionService.getSentRequests(resolveUser(principal));
    }

    @GetMapping("/friends")
    public List<FriendDto> friends(Principal principal) {
        return connectionService.getFriends(resolveUser(principal));
    }

    @GetMapping("/requests/count")
    public Map<String, Long> incomingCount(Principal principal) {
        return Map.of("count", connectionService.getIncomingCount(resolveUser(principal)));
    }

    private User resolveUser(Principal principal) {
        return userRepository.findByEmail(principal.getName().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
