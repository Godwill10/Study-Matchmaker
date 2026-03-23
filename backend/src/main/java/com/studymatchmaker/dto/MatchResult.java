package com.studymatchmaker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MatchResult {

    private UserProfileDto user;
    private double score;
    private List<String> reasons;
}