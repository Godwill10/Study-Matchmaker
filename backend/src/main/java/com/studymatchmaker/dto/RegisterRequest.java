package com.studymatchmaker.dto;

public record RegisterRequest(
        String email,
        String password,
        String displayName
) {}