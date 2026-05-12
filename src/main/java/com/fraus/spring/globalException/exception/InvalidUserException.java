package com.fraus.spring.globalException.exception;

public class InvalidUserException extends Exception {
    public InvalidUserException(String message) {
        super(message);
    }
}
