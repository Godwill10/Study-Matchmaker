package com.studymatchmaker.dto;

public record LoginRequest(
        String email,
        String password
) {}