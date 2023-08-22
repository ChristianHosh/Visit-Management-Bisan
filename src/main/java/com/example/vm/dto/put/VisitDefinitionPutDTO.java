package com.example.vm.dto.put;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class VisitDefinitionPutDTO {

    @Size(min = 8, max = 30, message = "Invalid Name: Must be of 8 - 30 characters")
    String name;

    @Size(min = 3, max = 255, message = "Invalid Description: Must be of 3 - 255 characters")
    String description;

    Long typeId;

    @Min(value = 1, message = "Invalid Frequency: Equals to one")
    @Max(value = 365, message = "Invalid Frequency: Exceeds one year")
    Integer frequency;

    @NotNull(message = "Invalid AllowRecurring: AllowRecurring is NULL")
    Boolean allowRecurring;

    @Null(message = "Invalid enabled : Must be null")
    Integer enabled;
}
