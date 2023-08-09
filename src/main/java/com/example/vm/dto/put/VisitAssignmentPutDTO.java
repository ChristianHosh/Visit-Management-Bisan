package com.example.vm.dto.put;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
@Builder
public class VisitAssignmentPutDTO {

    @FutureOrPresent(message = "Invalid Date: Date must be in the future or present")
    private Date date;

    @Size(max = 255, message = "Invalid Comment: Must be a maximum of 255 characters")
    private String comment;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;
}
