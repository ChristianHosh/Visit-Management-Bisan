package com.example.vm.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Date;

@Getter
@AllArgsConstructor
public class VisitAssignmentRequest {

    @NotNull
    private Date date;

    @NotNull
    @Size
    private String comment;

}
