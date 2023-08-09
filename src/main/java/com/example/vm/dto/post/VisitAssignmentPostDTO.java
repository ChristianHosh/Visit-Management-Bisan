package com.example.vm.dto.post;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class VisitAssignmentPostDTO {

    @Null(message = "Invalid createdTime : Automatically generated")
    Timestamp createdTime;

    @Null(message = "Invalid lastModifiedTime : Automatically generated")
    Timestamp lastModifiedTime;

    @NotNull(message = "Invalid Date: Date in NULL")
    @FutureOrPresent(message = "Invalid Date: Date must be in the future or present")
    private Date date;

    @NotNull(message = "Invalid Comment: Comment is NULL")
    @Size(max = 255, message = "Invalid Comment: Must be a maximum of 255 characters")
    private String comment;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;
}
