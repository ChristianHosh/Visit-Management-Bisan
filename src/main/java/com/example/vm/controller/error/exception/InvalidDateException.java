package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class InvalidDateException extends RuntimeException{
    public InvalidDateException(ErrorMessage message) {
        super(message.toString());
    }
}
