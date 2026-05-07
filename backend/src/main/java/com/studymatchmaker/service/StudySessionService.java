package com.studymatchmaker.service;

import com.studymatchmaker.dto.CreateSessionRequest;
import com.studymatchmaker.dto.StudySessionDto;
import com.studymatchmaker.exception.BadRequestException;
import com.studymatchmaker.exception.NotFoundException;
import com.studymatchmaker.model.StudySession;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.FriendshipRepository;
import com.studymatchmaker.repository.StudySessionRepository;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class StudySessionService {

    private final StudySessionRepository sessionRepository;
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final MapperService mapperService;

    @Transactional
    public StudySessionDto create(User host, CreateSessionRequest request) {
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start time must be in the future");
        }

        int maxParticipants = request.getMaxParticipants() > 0 ? request.getMaxParticipants() : 10;
        Set<User> invitedFriends = new HashSet<>();

        if (request.getInvitedFriendIds() != null && !request.getInvitedFriendIds().isEmpty()) {
            if (request.getInvitedFriendIds().size() + 1 > maxParticipants) {
                throw new BadRequestException("Invited friends exceed the maximum participant limit");
            }

            for (Long friendId : request.getInvitedFriendIds()) {
                if (friendId.equals(host.getId())) {
                    throw new BadRequestException("You cannot invite yourself");
                }

                if (!friendshipRepository.areFriendsByIds(host.getId(), friendId)) {
                    throw new BadRequestException("You can only invite people you are connected with");
                }

                User friend = userRepository.findById(friendId)
                        .orElseThrow(() -> new BadRequestException("Invited friend not found"));

                invitedFriends.add(friend);
            }
        }

        StudySession session = StudySession.builder()
                .host(host)
                .participants(invitedFriends)
                .title(request.getTitle())
                .course(request.getCourse())
                .topic(request.getTopic())
                .description(request.getDescription())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .location(request.getLocation())
                .mode(request.getMode())
                .maxParticipants(maxParticipants)
                .build();

        return toDto(sessionRepository.save(session), host);
    }

    @Transactional
    public StudySessionDto update(User user, Long sessionId, CreateSessionRequest request) {
        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (!session.getHost().getId().equals(user.getId())) {
            throw new BadRequestException("Only the host can edit this session");
        }

        if (session.getStatus() != StudySession.Status.UPCOMING) {
            throw new BadRequestException("Only upcoming sessions can be edited");
        }

        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Start time must be in the future");
        }

        int maxParticipants = request.getMaxParticipants() > 0 ? request.getMaxParticipants() : 10;
        if (maxParticipants < session.getCurrentParticipantCount()) {
            throw new BadRequestException("Max participants cannot be less than the current participant count");
        }

        session.setTitle(request.getTitle());
        session.setCourse(request.getCourse());
        session.setTopic(request.getTopic());
        session.setDescription(request.getDescription());
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setLocation(request.getLocation());
        session.setMode(request.getMode());
        session.setMaxParticipants(maxParticipants);

        return toDto(sessionRepository.save(session), user);
    }

    @Transactional
    public StudySessionDto join(User user, Long sessionId) {
        StudySession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new NotFoundException("Session not found"));

        if (session.getHost().getId().equals(user.getId())) {
            throw new BadRequestException("You are the host of this session");
        }

        if (!friendshipRepository.areFriendsByIds(user.getId(), session.getHost().getId())) {
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
        List<User> friends = friendshipRepository.findFriendUsersByUserId(currentUser.getId());
        List<Long> visibleHostIds = new ArrayList<>(
                friends.stream()
                        .map(User::getId)
                        .toList()
        );
        visibleHostIds.add(currentUser.getId());

        return sessionRepository.findUpcoming(LocalDateTime.now()).stream()
                .filter(s -> visibleHostIds.contains(s.getHost().getId()))
                .map(s -> toDto(s, currentUser))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudySessionDto> getMySessions(User currentUser) {
        return sessionRepository.findMyUpcomingByUserId(currentUser.getId(), LocalDateTime.now()).stream()
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
