package com.studymatchmaker.controller;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/reference")
@CrossOrigin(origins = "*")
public class ReferenceDataController {

    @GetMapping("/states")
    public List<Map<String, String>> getStates() {
        return List.of(
                state("AL","Alabama"),
                state("AK","Alaska"),
                state("AZ","Arizona"),
                state("AR","Arkansas"),
                state("CA","California"),
                state("CO","Colorado"),
                state("CT","Connecticut"),
                state("DE","Delaware"),
                state("FL","Florida"),
                state("GA","Georgia"),
                state("HI","Hawaii"),
                state("ID","Idaho"),
                state("IL","Illinois"),
                state("IN","Indiana"),
                state("IA","Iowa"),
                state("KS","Kansas"),
                state("KY","Kentucky"),
                state("LA","Louisiana"),
                state("ME","Maine"),
                state("MD","Maryland"),
                state("MA","Massachusetts"),
                state("MI","Michigan"),
                state("MN","Minnesota"),
                state("MS","Mississippi"),
                state("MO","Missouri"),
                state("MT","Montana"),
                state("NE","Nebraska"),
                state("NV","Nevada"),
                state("NH","New Hampshire"),
                state("NJ","New Jersey"),
                state("NM","New Mexico"),
                state("NY","New York"),
                state("NC","North Carolina"),
                state("ND","North Dakota"),
                state("OH","Ohio"),
                state("OK","Oklahoma"),
                state("OR","Oregon"),
                state("PA","Pennsylvania"),
                state("RI","Rhode Island"),
                state("SC","South Carolina"),
                state("SD","South Dakota"),
                state("TN","Tennessee"),
                state("TX","Texas"),
                state("UT","Utah"),
                state("VT","Vermont"),
                state("VA","Virginia"),
                state("WA","Washington"),
                state("WV","West Virginia"),
                state("WI","Wisconsin"),
                state("WY","Wyoming")
        );
    }

    private Map<String, String> state(String code, String label) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        map.put("label", label);
        return map;
    }

    @GetMapping("/universities")
    public List<String> getUniversities(@RequestParam String state) {
        Map<String, List<String>> data = new HashMap<>();

        data.put("MN", List.of("University of St Thomas", "University of Minnesota"));
        data.put("CA", List.of("UCLA", "Stanford", "UC Berkeley"));
        data.put("TX", List.of("UT Austin", "Texas A&M"));
        data.put("NY", List.of("NYU", "Columbia"));

        return data.getOrDefault(state, List.of());
    }

    @GetMapping("/majors")
    public List<Map<String, String>> getMajors() {
        return List.of(
                Map.of("label", "Computer Science"),
                Map.of("label", "Engineering"),
                Map.of("label", "Business"),
                Map.of("label", "Biology"),
                Map.of("label", "Mathematics")
        );
    }

    @GetMapping("/academic-levels")
    public List<Map<String, String>> getAcademicLevels() {
        return List.of(
                Map.of("label", "Freshman"),
                Map.of("label", "Sophomore"),
                Map.of("label", "Junior"),
                Map.of("label", "Senior"),
                Map.of("label", "Graduate")
        );
    }

    @GetMapping("/study-styles")
    public List<Map<String, String>> getStudyStyles() {
        return List.of(
                Map.of("label", "Solo"),
                Map.of("label", "Collaborative"),
                Map.of("label", "Hands-on"),
                Map.of("label", "Lecture-based")
        );
    }

    @GetMapping("/courses")
    public List<String> getCourses(@RequestParam String academicLevel) {
        Map<String, List<String>> data = new HashMap<>();

        data.put("Freshman", List.of("Intro to Programming", "Calculus I"));
        data.put("Sophomore", List.of("Data Structures", "Calculus II"));
        data.put("Junior", List.of("Algorithms", "Databases"));
        data.put("Senior", List.of("Distributed Systems", "AI"));
        data.put("Graduate", List.of("Machine Learning", "Advanced AI"));

        return data.getOrDefault(academicLevel, List.of());
    }
}