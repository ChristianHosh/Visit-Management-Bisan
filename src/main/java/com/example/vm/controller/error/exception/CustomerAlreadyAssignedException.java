package com.example.vm.controller.error.exception;

public class CustomerAlreadyAssignedException extends RuntimeException {
    public static final String CUSTOMER_ALREADY_ASSIGNED = "Invalid Customer: Customer is already assigned to this assignment";
    public CustomerAlreadyAssignedException() {
        super(CUSTOMER_ALREADY_ASSIGNED);
    }
}
