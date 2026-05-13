package com.fraus.spring.user.web.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest (
        @NotBlank
        @Size(max = 10)
        String username,

        @Email
        @NotBlank
        @Size(max = 30)
        String email,

        @Size(min = 6, max = 60)
        @NotBlank
        String password
){
}
