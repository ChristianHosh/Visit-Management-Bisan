package com.example.vm.controller.error.exception;

public class NoContactTypeException extends RuntimeException{

    public static final String ASSIGNMENT_INVALID_CONTACT_TYPES = "Invalid Assignment: Contacts for this Type are not Available ";
    public NoContactTypeException() {
        super(ASSIGNMENT_INVALID_CONTACT_TYPES);
    }
}
