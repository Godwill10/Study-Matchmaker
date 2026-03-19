package com.studymatchmaker.controller;

import com.studymatchmaker.dto.MatchRequest;
import com.studymatchmaker.dto.MatchResponse;
import com.studymatchmaker.service.StudySessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-sessions")
public class StudySessionController {

    private final StudySessionService service;

    public StudySessionController(StudySessionService service) {
        this.service = service;
    }

    @PostMapping("/match")
    public List<MatchResponse> matchSessions(@RequestBody MatchRequest request) {
        return service.findMatches(request);
    }
}