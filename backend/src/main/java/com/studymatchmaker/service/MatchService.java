package com.studymatchmaker.service;

import com.studymatchmaker.model.User;
import com.studymatchmaker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final UserRepository userRepository;

    public List<Map<String, Object>> findMatches(User currentUser) {

        List<User> allUsers = userRepository.findAll();

        List<Map<String, Object>> results = new ArrayList<>();

        for (User other : allUsers) {

            if (other.getId().equals(currentUser.getId())) continue;

            int score = 0;
            List<String> reasons = new ArrayList<>();

            // ✅ COURSES MATCH (MOST IMPORTANT)
            List<String> sharedCourses = new ArrayList<>(currentUser.getCourses());
            sharedCourses.retainAll(other.getCourses());

            if (!sharedCourses.isEmpty()) {
                score += sharedCourses.size() * 5;
                reasons.add("Shared courses: " + String.join(", ", sharedCourses));
            }

            // ✅ MAJOR
            if (Objects.equals(currentUser.getMajor(), other.getMajor())) {
                score += 3;
                reasons.add("Same major");
            }

            // ✅ LEVEL
            if (Objects.equals(currentUser.getAcademicLevel(), other.getAcademicLevel())) {
                score += 2;
                reasons.add("Same academic level");
            }

            // ✅ STUDY STYLE
            if (Objects.equals(currentUser.getStudyStyle(), other.getStudyStyle())) {
                score += 1;
                reasons.add("Same study style");
            }

            // ✅ SCHOOL
            if (Objects.equals(currentUser.getSchool(), other.getSchool())) {
                score += 1;
                reasons.add("Same university");
            }

            Map<String, Object> match = new HashMap<>();
            match.put("user", other);
            match.put("score", score);
            match.put("reasons", reasons);

            results.add(match);
        }

        // 🔥 SORT BEST MATCHES FIRST
        results.sort((a, b) -> (int) b.get("score") - (int) a.get("score"));

        return results;
    }
}