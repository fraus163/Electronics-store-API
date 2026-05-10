package com.fraus.spring.user.web.Dto;

public record LoginRequest (
        String username,
        String password
) {
}
