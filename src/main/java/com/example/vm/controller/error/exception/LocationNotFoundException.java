package com.example.vm.controller.error.exception;

public class LocationNotFoundException extends RuntimeException{
    private static final String NOT_FOUND_MESSAGE = "Invalid Address: Location not found";
    public LocationNotFoundException() {
        super(NOT_FOUND_MESSAGE);
    }
}
