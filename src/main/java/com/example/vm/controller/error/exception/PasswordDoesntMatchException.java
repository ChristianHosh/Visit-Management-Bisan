package com.example.vm.controller.error.exception;

public class PasswordDoesntMatchException extends RuntimeException {
    public static final String PASSWORD_DOES_NOT_MATCH = "Invalid Password: Password does not match Confirm Password ";
    public PasswordDoesntMatchException(String message) {
        super(message);
    }
}
