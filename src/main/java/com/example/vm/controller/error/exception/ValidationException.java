package com.example.vm.controller.error.exception;

public class ValidationException extends RuntimeException {
    public static final String PASSWORD_DOES_NOT_MATCH = "Password Does Not match Confirm Password ";
    public ValidationException(String message) {
        super(message);
    }
}
