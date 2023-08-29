package com.example.vm.controller.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
public class ApiError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy | hh:mm:s")
    private Timestamp timestamp;

    private Integer status;

    private String message;

    public ApiError() {
        this.timestamp = Timestamp.from(Instant.now());
    }

    public ApiError(HttpStatus httpStatus, String message) {
        this();
        this.status = httpStatus.value();
        this.message = message;
    }

}
