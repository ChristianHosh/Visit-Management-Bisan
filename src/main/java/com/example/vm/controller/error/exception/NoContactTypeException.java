package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class NoContactTypeException extends RuntimeException{
    public NoContactTypeException() {
        super(ErrorMessage.INVALID_ASSIGNMENT_CONTACT_TYPES.toString());
    }
}
