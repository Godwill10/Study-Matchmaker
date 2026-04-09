package com.studymatchmaker.service;

import com.studymatchmaker.dto.MatchRequest;
import com.studymatchmaker.dto.MatchResult;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final UserRepository userRepository;
    private final MapperService mapperService;

    public List<MatchResult> matchUsers(User currentUser, MatchRequest request) {
        return userRepository.findAll().stream()
                .filter(candidate -> !candidate.getId().equals(currentUser.getId()))
                .filter(candidate -> matchesFilters(candidate, request))
                .map(candidate -> scoreCandidate(currentUser, candidate, request))
                .filter(result -> result.getScore() > 0)
                .sorted(Comparator.comparingDouble(MatchResult::getScore).reversed())
                .limit(25)
                .toList();
    }

    private MatchResult scoreCandidate(User currentUser, User candidate, MatchRequest request) {
        double score = 0;
        List<String> reasons = new ArrayList<>();

        if (equalsIgnoreCase(candidate.getSchool(), currentUser.getSchool())) {
            score += 22;
            reasons.add("Same university");
        }

        if (equalsIgnoreCase(candidate.getAcademicLevel(), currentUser.getAcademicLevel())) {
            score += 15;
            reasons.add("Same academic level");
        }

        if (equalsIgnoreCase(candidate.getStudyStyle(), currentUser.getStudyStyle())) {
            score += 12;
            reasons.add("Similar study style");
        }

        if (equalsIgnoreCase(candidate.getPreferredMode(), currentUser.getPreferredMode())) {
            score += 10;
            reasons.add("Same preferred mode");
        }

        long overlap = candidate.getCourses().stream()
                .filter(c -> currentUser.getCourses().stream().anyMatch(u -> u.equalsIgnoreCase(c)))
                .count();

        if (overlap > 0) {
            score += Math.min(30, overlap * 10);
            reasons.add(overlap + " overlapping course" + (overlap > 1 ? "s" : ""));
        }

        if (Boolean.TRUE.equals(candidate.getLocationVisible())
                && equalsIgnoreCase(candidate.getState(), currentUser.getState())) {
            score += 5;
            reasons.add("Same state");
        }

        if (Boolean.TRUE.equals(candidate.getLocationVisible())
                && equalsIgnoreCase(candidate.getCity(), currentUser.getCity())
                && currentUser.getCity() != null) {
            score += 8;
            reasons.add("Same city");
        }
        return new MatchResult(
                mapperService.toUserProfile(candidate),
                score,
                reasons
        );
    }

    private boolean matchesFilters(User candidate, MatchRequest request) {
        if (request == null) {
            return true;
        }
        if (request.getSchool() != null && !equalsIgnoreCase(candidate.getSchool(), request.getSchool())) {
            return false;
        }
        if (request.getPreferredMode() != null && !equalsIgnoreCase(candidate.getPreferredMode(), request.getPreferredMode())) {
            return false;
        }
        if (request.getStudyStyle() != null && !equalsIgnoreCase(candidate.getStudyStyle(), request.getStudyStyle())) {
            return false;
        }
        if (Boolean.TRUE.equals(request.getRequireVisibleLocation()) && !Boolean.TRUE.equals(candidate.getLocationVisible())) {
            return false;
        }
        if (request.getState() != null && !equalsIgnoreCase(candidate.getState(), request.getState())) {
            return false;
        }
        if (request.getCity() != null && !equalsIgnoreCase(candidate.getCity(), request.getCity())) {
            return false;
        }
        if (request.getCourses() != null && !request.getCourses().isEmpty()) {
            boolean hasOverlap = candidate.getCourses().stream()
                    .anyMatch(course -> request.getCourses().stream().anyMatch(filter -> equalsIgnoreCase(course, filter)));
            if (!hasOverlap) {
                return false;
            }
        }
        return true;
    }

    private boolean equalsIgnoreCase(String a, String b) { return a != null && b != null && a.trim().equalsIgnoreCase(b.trim()); }
}
