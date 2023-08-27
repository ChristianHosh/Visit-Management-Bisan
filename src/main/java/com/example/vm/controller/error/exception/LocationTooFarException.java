package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class LocationTooFarException extends RuntimeException {
    public LocationTooFarException() {
        super(ErrorMessage.INVALID_LOCATION_IS_TOO_FAR.toString());
    }
}
