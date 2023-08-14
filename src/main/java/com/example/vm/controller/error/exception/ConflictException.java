package com.example.vm.controller.error.exception;

public class ConflictException extends RuntimeException {
    public static final String CUSTOMER_ALREADY_ASSIGNED = "Customer is already in this assignment";

    public ConflictException() {
        super(CUSTOMER_ALREADY_ASSIGNED);
    }
}
