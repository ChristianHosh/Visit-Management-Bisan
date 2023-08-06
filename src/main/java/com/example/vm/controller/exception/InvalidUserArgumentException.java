package com.example.vm.controller.exception;

public class InvalidUserArgumentException extends Exception{

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
