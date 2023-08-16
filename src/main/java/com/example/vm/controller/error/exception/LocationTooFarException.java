package com.example.vm.controller.error.exception;

public class LocationTooFarException extends RuntimeException {
    private static final String NOT_FOUND_MESSAGE = "Invalid Location: Location too far from customer";
    public LocationTooFarException() {
        super(NOT_FOUND_MESSAGE);
    }
}
