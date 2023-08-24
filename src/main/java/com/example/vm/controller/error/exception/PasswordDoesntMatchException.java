package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessages;

public class PasswordDoesntMatchException extends RuntimeException {
    public PasswordDoesntMatchException() {
        super(ErrorMessages.PASSWORD_DOES_NOT_MATCH);
    }
}
