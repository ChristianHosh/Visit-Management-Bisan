package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessages;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException() {
        super(ErrorMessages.USER_ALREADY_EXISTS);
    }

}
