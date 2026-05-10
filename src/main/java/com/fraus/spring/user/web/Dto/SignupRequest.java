package com.fraus.spring.user.web.Dto;

public record SignupRequest (
        String username,
        String email,
        String password
){
}
