package com.studymatchmaker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendConnectionRequest {
    @NotNull
    private Long receiverId;
}
