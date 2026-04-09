package com.studymatchmaker.controller;

import com.studymatchmaker.dto.CreateSessionRequest;
import com.studymatchmaker.dto.StudySessionDto;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import com.studymatchmaker.service.StudySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
@RequiredArgsConstructor
public class StudySessionController {

    private final StudySessionService sessionService;
    private final UserRepository userRepository;

    @PostMapping
    public StudySessionDto create(Principal principal, @Valid @RequestBody CreateSessionRequest request) {
        return sessionService.create(resolveUser(principal), request);
    }

    @GetMapping("/upcoming")
    public List<StudySessionDto> upcoming(Principal principal) {
        return sessionService.getUpcoming(resolveUser(principal));
    }

    @GetMapping("/mine")
    public List<StudySessionDto> mine(Principal principal) {
        return sessionService.getMySessions(resolveUser(principal));
    }

    @GetMapping("/hosted")
    public List<StudySessionDto> hosted(Principal principal) {
        return sessionService.getHosted(resolveUser(principal));
    }

    @GetMapping("/{id}")
    public StudySessionDto getById(Principal principal, @PathVariable Long id) {
        return sessionService.getById(resolveUser(principal), id);
    }

    @PostMapping("/{id}/join")
    public StudySessionDto join(Principal principal, @PathVariable Long id) {
        return sessionService.join(resolveUser(principal), id);
    }

    @PostMapping("/{id}/leave")
    public StudySessionDto leave(Principal principal, @PathVariable Long id) {
        return sessionService.leave(resolveUser(principal), id);
    }

    @PostMapping("/{id}/cancel")
    public StudySessionDto cancel(Principal principal, @PathVariable Long id) {
        return sessionService.cancel(resolveUser(principal), id);
    }

    private User resolveUser(Principal principal) {
        return userRepository.findByEmail(principal.getName().toLowerCase().trim())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
