package com.example.vm.controller.error;

import lombok.Data;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;

@Data
public class UserErrorResponse {

    private HttpStatus status;
    private String message;
    private Timestamp timestamp;

    public UserErrorResponse() {

    }

    public UserErrorResponse(HttpStatus status, String message, Timestamp timestamp) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
    }
}
