package com.studymatchmaker.dto;

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
public class FriendDto {
    private UserProfileDto user;
    private LocalDateTime connectedAt;
    private List<String> sharedCourses;
}
