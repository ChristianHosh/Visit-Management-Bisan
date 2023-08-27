package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class PasswordDoesntMatchException extends RuntimeException {

    public PasswordDoesntMatchException() {
        super(ErrorMessage.PASSWORDS_DO_NOT_MATCH.toString());
    }
}
