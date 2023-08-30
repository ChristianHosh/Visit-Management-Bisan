package com.example.vm.controller.error.exception;

import com.example.vm.controller.error.ErrorMessage;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException() {
        super(ErrorMessage.LOCATION_NOT_FOUND.toString());
    }
}
