package com.example.vm.controller.error.exception;

public class InvalidUserArgumentException extends RuntimeException{

    public InvalidUserArgumentException(String message) {
        super(message);
    }

    public InvalidUserArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidUserArgumentException(Throwable cause) {
        super(cause);
    }
}
