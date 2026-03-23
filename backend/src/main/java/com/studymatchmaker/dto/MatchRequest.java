package com.studymatchmaker.dto;

import lombok.Data;

import java.util.Set;

@Data
public class MatchRequest {
    private String school;
    private Set<String> courses;
    private String preferredMode;
    private String studyStyle;
    private Boolean requireVisibleLocation;
    private String city;
    private String state;
}
