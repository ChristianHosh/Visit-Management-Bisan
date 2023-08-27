package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class EntityNotEnabled extends RuntimeException{
    public EntityNotEnabled(ErrorMessage message) {
        super(message.toString());
    }
}
