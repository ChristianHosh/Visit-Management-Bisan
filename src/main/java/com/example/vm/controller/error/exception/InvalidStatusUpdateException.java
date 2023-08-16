package com.example.vm.controller.error.exception;

public class InvalidStatusUpdateException extends RuntimeException{

    private static final String NOT_FOUND_MESSAGE = "Invalid Status: Status can't be changed";
    public InvalidStatusUpdateException() {
        super(NOT_FOUND_MESSAGE);
    }
}
