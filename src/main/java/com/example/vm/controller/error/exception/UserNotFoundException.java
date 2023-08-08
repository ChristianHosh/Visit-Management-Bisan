package com.example.vm.controller.error.exception;

public class UserNotFoundException extends RuntimeException {

    private static final String NOT_FOUND_MESSAGE = "Invalid ID : Not Found";

    public UserNotFoundException() {
        super(NOT_FOUND_MESSAGE);
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
    }
}
