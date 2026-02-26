package com.studymatchmaker.exception;

import java.time.LocalDateTime;

public record ApiError(
        LocalDateTime timestamp,
        String path,
        String message
) {}