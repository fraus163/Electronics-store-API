package com.fraus.spring.globalException;

import java.time.LocalDateTime;

public record ErrorResponseDto (
        String message,
        String detailedMessage,
        LocalDateTime errorTime
) {
}