package com.studymatchmaker.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudySessionDto {
    private Long id;
    private UserProfileDto host;
    private List<UserProfileDto> participants;
    private String title;
    private String course;
    private String topic;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private String mode;
    private int maxParticipants;
    private int currentParticipantCount;
    private String status;
    private LocalDateTime createdAt;

    @JsonProperty("isHost")
    private boolean isHost;

    @JsonProperty("isParticipant")
    private boolean isParticipant;

    @JsonProperty("isFull")
    private boolean isFull;
}
