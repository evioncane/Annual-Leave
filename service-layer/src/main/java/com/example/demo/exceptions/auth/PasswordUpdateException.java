package com.example.demo.exceptions.auth;

public class PasswordUpdateException extends RuntimeException {
    public PasswordUpdateException(String message) {
        super(message);
    }
}
