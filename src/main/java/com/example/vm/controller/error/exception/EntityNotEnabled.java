package com.example.vm.controller.error.exception;

public class EntityNotEnabled extends RuntimeException{
    public EntityNotEnabled(String message) {
        super(message);
    }
}
