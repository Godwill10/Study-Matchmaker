package com.studymatchmaker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfileDto {
    private Long id;
    private String fullName;
    private String email;
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
    private String profileImageUrl;
}
