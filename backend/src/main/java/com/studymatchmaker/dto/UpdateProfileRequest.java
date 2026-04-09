package com.studymatchmaker.dto;

import lombok.Data;

import java.util.List;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String school;
    private String major;
    private String academicLevel;
    private String bio;
    private String studyStyle;
    private String preferredMode;
    private List<String> courses;
    private Boolean locationVisible;
    private String city;
    private String state;
    private Double latitude;
    private Double longitude;
}
