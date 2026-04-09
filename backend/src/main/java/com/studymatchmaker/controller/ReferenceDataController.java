package com.studymatchmaker.controller;

import com.studymatchmaker.service.ReferenceDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reference")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceData;

    @GetMapping("/states")
    public List<Map<String, String>> getStates() {
        return referenceData.getStates();
    }

    @GetMapping("/universities")
    public List<String> getUniversities(@RequestParam String state) {
        return referenceData.getUniversities(state);
    }

    @GetMapping("/majors")
    public List<Map<String, String>> getMajors() {
        return referenceData.getMajors().stream()
                .map(m -> Map.of("label", m))
                .toList();
    }

    @GetMapping("/academic-levels")
    public List<Map<String, String>> getAcademicLevels() {
        return referenceData.getAcademicLevels().stream()
                .map(l -> Map.of("label", l))
                .toList();
    }

    @GetMapping("/study-styles")
    public List<Map<String, String>> getStudyStyles() {
        return referenceData.getStudyStyles().stream()
                .map(s -> Map.of("label", s))
                .toList();
    }

    @GetMapping("/courses")
    public List<String> getCourses(
            @RequestParam String academicLevel,
            @RequestParam(required = false, defaultValue = "") String major) {
        return referenceData.getCourses(major, academicLevel);
    }
}
