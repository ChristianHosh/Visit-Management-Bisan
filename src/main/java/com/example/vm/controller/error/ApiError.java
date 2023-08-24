package com.example.vm.controller.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiError {

    private HttpStatus httpStatus;
    private String path;
    private String message;
}
