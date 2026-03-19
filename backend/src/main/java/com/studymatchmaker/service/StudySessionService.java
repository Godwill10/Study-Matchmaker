package com.studymatchmaker.service;

import com.studymatchmaker.dto.MatchRequest;
import com.studymatchmaker.dto.MatchResponse;
import com.studymatchmaker.model.StudySession;
import com.studymatchmaker.repository.StudySessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudySessionService {

    private final StudySessionRepository repository;

    public StudySessionService(StudySessionRepository repository) {
        this.repository = repository;
    }

    public List<MatchResponse> findMatches(MatchRequest request) {

        if (request.getTopic() == null || request.getTopic().isBlank()) {
            throw new RuntimeException("Topic is required for matching");
        }

        List<StudySession> sessions = repository.findAll();

        return sessions.stream()
                .map(session -> {
                    int score = calculateScore(session,
                            request.getTopic(),
                            request.getLevel(),
                            request.getMode());

                    return new MatchResponse(
                            session.getId(),
                            session.getTopic(),
                            session.getLevel(),
                            session.getMode(),
                            score
                    );
                })
                .filter(match -> match.getScore() > 0) // remove irrelevant matches
                .sorted((a, b) -> b.getScore() - a.getScore())
                .limit(5) // 🔥 top 5 matches
                .collect(Collectors.toList());
    }

    private int calculateScore(StudySession session, String topic, String level, String mode) {
        int score = 0;

        if (session.getTopic() != null && topic != null) {
            if (session.getTopic().equalsIgnoreCase(topic)) {
                score += 60;
            } else if (session.getTopic().toLowerCase().contains(topic.toLowerCase())
                    || topic.toLowerCase().contains(session.getTopic().toLowerCase())) {
                score += 40;
            }
        }

        if (session.getLevel() != null && level != null) {
            if (session.getLevel().equalsIgnoreCase(level)) {
                score += 20;
            }
        }

        if (session.getMode() != null && mode != null) {
            if (session.getMode().equalsIgnoreCase(mode)) {
                score += 15;
            }
        }

        return score;
    }
}