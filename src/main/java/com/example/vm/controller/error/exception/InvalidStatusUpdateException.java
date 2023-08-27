package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class InvalidStatusUpdateException extends RuntimeException {

    public InvalidStatusUpdateException() {
        super(ErrorMessage.INVALID_FORM_STATUS_UPDATE.toString());
    }
}
