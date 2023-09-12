package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class InvalidPasswordResetException extends RuntimeException {
    public InvalidPasswordResetException(ErrorMessage error) {
        super(error.message);
    }
}
