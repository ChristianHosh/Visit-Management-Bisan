package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class UserAlreadyExistsException extends RuntimeException{

    public UserAlreadyExistsException() {
        super(ErrorMessage.USER_ALREADY_EXISTS.toString());
    }

}
