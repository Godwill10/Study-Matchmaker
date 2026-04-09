package com.studymatchmaker.service;

import com.studymatchmaker.dto.CreateSessionRequest;
import com.studymatchmaker.dto.StudySessionDto;
import com.studymatchmaker.exception.BadRequestException;
import com.studymatchmaker.exception.NotFoundException;
import com.studymatchmaker.model.StudySession;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.FriendshipRepository;
import com.studymatchmaker.repository.StudySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository sessionRepository;
    private final FriendshipRepository friendshipRepository;
    private final MapperService mapperService;

    @Transactional
    public StudySessionDto create(User host, CreateSessionRequest request) {
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start time must be in the future");
        }

        StudySession session = StudySession.builder()
                .host(host)
                .title(request.getTitle())
                .course(request.getCourse())
                .topic(request.getTopic())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .mode(request.getMode())
                .maxParticipants(request.getMaxParticipants() > 0 ? request.getMaxParticipants() : 10)
                .build();

        return toDto(sessionRepository.save(session), host);
    }

    @Transactional
    public StudySessionDto join(User user, Long sessionId) {
        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (session.getHost().getId().equals(user.getId())) {
            throw new BadRequestException("You are the host of this session");
        }

        if (!friendshipRepository.areFriends(user, session.getHost())) {
            throw new BadRequestException("You can only join sessions from your connections");
        }

        if (session.getParticipants().stream().anyMatch(p -> p.getId().equals(user.getId()))) {
            throw new BadRequestException("You have already joined this session");
        }

        if (session.isFull()) {
            throw new BadRequestException("This session is full");
        }

        if (session.getStatus() != StudySession.Status.UPCOMING) {
            throw new BadRequestException("Can only join upcoming sessions");
        }

        session.getParticipants().add(user);
        return toDto(sessionRepository.save(session), user);
    }

    @Transactional
    public StudySessionDto leave(User user, Long sessionId) {
        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (session.getHost().getId().equals(user.getId())) {
            throw new BadRequestException("Host cannot leave. Cancel the session instead.");
        }

        boolean removed = session.getParticipants().removeIf(p -> p.getId().equals(user.getId()));
        if (!removed) {
            throw new BadRequestException("You are not a participant of this session");
        }

        return toDto(sessionRepository.save(session), user);
    }

    @Transactional
    public StudySessionDto cancel(User user, Long sessionId) {
        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (!session.getHost().getId().equals(user.getId())) {
            throw new BadRequestException("Only the host can cancel a session");
        }

        session.setStatus(StudySession.Status.CANCELLED);
        return toDto(sessionRepository.save(session), user);
    }

    @Transactional(readOnly = true)
    public List<StudySessionDto> getUpcoming(User currentUser) {
        List<User> friends = friendshipRepository.findFriendUsers(currentUser);
        List<User> visibleHosts = new ArrayList<>(friends);
        visibleHosts.add(currentUser);
        return sessionRepository.findUpcomingByHosts(visibleHosts, LocalDateTime.now()).stream()
                .map(s -> toDto(s, currentUser))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudySessionDto> getMySessions(User currentUser) {
        return sessionRepository.findMyUpcoming(currentUser, LocalDateTime.now()).stream()
                .map(s -> toDto(s, currentUser))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudySessionDto> getHosted(User host) {
        return sessionRepository.findByHostOrderByStartTimeDesc(host).stream()
                .map(s -> toDto(s, host))
                .toList();
    }

    @Transactional(readOnly = true)
    public StudySessionDto getById(User currentUser, Long id) {
        StudySession session = sessionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Session not found"));
        return toDto(session, currentUser);
    }

    private StudySessionDto toDto(StudySession s, User currentUser) {
        return StudySessionDto.builder()
                .id(s.getId())
                .host(mapperService.toUserProfile(s.getHost()))
                .participants(s.getParticipants().stream()
                        .map(mapperService::toUserProfile)
                        .toList())
                .title(s.getTitle())
                .course(s.getCourse())
                .topic(s.getTopic())
                .description(s.getDescription())
                .startTime(s.getStartTime())
                .endTime(s.getEndTime())
                .location(s.getLocation())
                .mode(s.getMode())
                .maxParticipants(s.getMaxParticipants())
                .currentParticipantCount(s.getCurrentParticipantCount())
                .status(s.getStatus().name())
                .createdAt(s.getCreatedAt())
                .isHost(s.getHost().getId().equals(currentUser.getId()))
                .isParticipant(s.getParticipants().stream()
                        .anyMatch(p -> p.getId().equals(currentUser.getId())))
                .isFull(s.isFull())
                .build();
    }
}
