package com.example.vm.dto.post;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class VisitDefinitionPostDTO {
    @Null(message = "Invalid createdTime : Automatically generated")
    Timestamp createdTime;

    @Null(message = "Invalid lastModifiedTime : Automatically generated")
    Timestamp lastModifiedTime;


    @NotBlank(message = "Invalid Name: Empty Name ")
    @NotNull(message = "Invalid Name: Name is NULL")
    @Size(min = 8, max = 30, message = "Invalid Name: Must be of 8 - 30 characters")
    String name;

    @NotBlank(message = "Invalid Description: Empty Description ")
    @NotNull(message = "Invalid Description: Description is NULL")
    @Size(min = 3, max = 255, message = "Invalid Description: Must be of 3 - 255 characters")
    String description;

    @NotNull(message = "Invalid Type: Type is NULL")
    @Min(value = 0, message = "Invalid Type: Equals to zero")
    @Max(value = 2, message = "Invalid Type: Exceeds two")
    Integer type;
//    0 SALES
//    1 MARKETING
//    2 DELIVERY


    @NotNull(message = "Invalid Frequency: Frequency is NULL")
    @Min(value = 1, message = "Invalid Frequency: Equals to one")
    @Max(value = 365, message = "Invalid Frequency: Exceeds one year")
    Integer frequency;

    @NotNull(message = "Invalid AllowRecurring: AllowRecurring is NULL")
    Boolean allowRecurring;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;
}
