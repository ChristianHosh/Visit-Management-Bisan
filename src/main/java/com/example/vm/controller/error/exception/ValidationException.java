package com.example.vm.controller.error.exception;

public class ValidationException extends RuntimeException {
    public static final String NOT_Match = "Password Does Not match Confirm Password ";
    public ValidationException(String notMatch) {
        super(notMatch)
    }
}
