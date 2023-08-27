package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class CustomerAlreadyAssignedException extends RuntimeException {

    public CustomerAlreadyAssignedException() {
        super(ErrorMessage.CUSTOMER_ALREADY_ASSIGNED.toString());
    }
}
