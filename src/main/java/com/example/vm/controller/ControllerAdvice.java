package com.example.vm.controller;

import com.example.vm.controller.error.UserErrorResponse;
import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler
    ResponseEntity<UserErrorResponse> handleOthers(Exception otherException) {
        UserErrorResponse errorResponse = new UserErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                otherException.getMessage(), Timestamp.from(Instant.now()));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException exception) {
        List<String> errors = exception.getBindingResult().getFieldErrors()
                .stream().map(FieldError::getDefaultMessage).collect(Collectors.toList());

        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }

}


