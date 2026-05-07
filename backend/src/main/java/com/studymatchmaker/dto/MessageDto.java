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
public class MessageDto {
    private Long id;
    private UserProfileDto sender;
    private UserProfileDto receiver;
    private String content;
    private LocalDateTime createdAt;
}