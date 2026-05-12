package com.fraus.spring.user.web.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest (
        @NotBlank
        String username,

        @NotBlank
        String password
) {
}
