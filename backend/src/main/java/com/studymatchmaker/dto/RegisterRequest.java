package com.studymatchmaker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class RegisterRequest {
    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String state;

    @NotBlank
    private String school;

    @NotBlank
    private String major;

    @NotBlank
    private String academicLevel;

    @NotBlank
    private String studyStyle;

    @NotEmpty
    private List<String> courses;

    private String preferredMode;
    private String city;
    private Boolean locationVisible;
    private Double latitude;
    private Double longitude;
    private String bio;
}
