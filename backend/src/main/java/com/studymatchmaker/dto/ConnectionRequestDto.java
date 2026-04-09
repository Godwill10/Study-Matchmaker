package com.studymatchmaker.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectionRequestDto {
    private Long id;
    private UserProfileDto sender;
    private UserProfileDto receiver;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime respondedAt;
}
