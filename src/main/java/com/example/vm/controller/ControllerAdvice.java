package com.example.vm.controller;

import com.example.vm.controller.error.UserErrorResponse;
import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {


    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException exception) {
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.NOT_FOUND,
                exception.getMessage(), Timestamp.from(Instant.now()));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(InvalidUserArgumentException exception) {
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.BAD_REQUEST,
                exception.getMessage(), Timestamp.from(Instant.now()));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserAlreadyExistsException exception) {
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.CONFLICT,
                exception.getMessage(), Timestamp.from(Instant.now()));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
