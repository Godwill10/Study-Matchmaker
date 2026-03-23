package com.studymatchmaker.service;

import com.studymatchmaker.dto.MatchRequest;
import com.studymatchmaker.dto.MatchResult;
import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.CompletedMatchRepository;
import com.studymatchmaker.repository.FriendshipRepository;
import com.studymatchmaker.repository.ReviewRepository;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchingService {
    private final UserRepository userRepository;
    private final MapperService mapperService;
    private final FriendshipRepository friendshipRepository;
    private final CompletedMatchRepository completedMatchRepository;
    private final ReviewRepository reviewRepository;

    public List<MatchResult> matchUsers(User currentUser, MatchRequest request) {
        return userRepository.findAll().stream()
                .filter(candidate -> !candidate.getId().equals(currentUser.getId()))
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

        if (!friendshipRepository.findByUserOneOrUserTwoOrderByCreatedAtDesc(currentUser, currentUser).isEmpty()) {
            score += 2;
        }

        long priorMatches = completedMatchRepository
                .findByUserOneOrUserTwoOrderByCompletedAtDesc(currentUser, currentUser)
                .stream()
                .filter(cm -> cm.getUserOne().getId().equals(candidate.getId())
                        || cm.getUserTwo().getId().equals(candidate.getId()))
                .count();

        if (priorMatches > 0) {
            score += 6;
            reasons.add("You have matched before");
        }

        double avgReview = reviewRepository
                .findByRevieweeOrderByCreatedAtDesc(candidate)
                .stream()
                .mapToInt(r -> r.getRating() == null ? 0 : r.getRating())
                .average()
                .orElse(0);

        if (avgReview >= 4.0) {
            score += 4;
            reasons.add("Highly rated by students");
        }

        // ✅ FINAL FIX
        return new MatchResult(
                mapperService.toUserProfile(candidate),
                score,
                reasons
        );
    }

    private boolean equalsIgnoreCase(String a, String b) { return a != null && b != null && a.trim().equalsIgnoreCase(b.trim()); }
}
