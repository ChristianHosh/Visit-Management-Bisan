package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(ErrorMessage message) {
        super(message.toString());
    }
}
