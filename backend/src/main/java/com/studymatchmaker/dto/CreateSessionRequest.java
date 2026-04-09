package com.studymatchmaker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateSessionRequest {
    @NotBlank
    private String title;

    private String course;
    private String topic;
    private String description;

    @NotNull
    private LocalDateTime startTime;

    private LocalDateTime endTime;
    private String location;

    @NotBlank
    private String mode;

    private int maxParticipants = 10;
}
