package com.example.vm.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@Getter
@AllArgsConstructor
public class VisitAssignmentRequest {

    @NotNull(message = "Bad request: date is null")
    private Date date;

    @NotNull(message = "Bad request: comment is null")
    @Size(min = 5, max = 30, message = "Bad request: comment must be between 5 and 30 characters long")
    private String comment;

}
