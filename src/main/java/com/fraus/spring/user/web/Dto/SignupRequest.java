package com.fraus.spring.user.web.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SignupRequest (
        @NotBlank
        String username,

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6)
        String password
){
}
