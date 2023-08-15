package com.example.vm.controller.error.exception;

public class UserAlreadyExistsException extends RuntimeException{

    private static final String ALREADY_EXISTS_MESSAGE = "Invalid Username: Already Exists";

    public UserAlreadyExistsException() {
        super(ALREADY_EXISTS_MESSAGE);
    }

}
