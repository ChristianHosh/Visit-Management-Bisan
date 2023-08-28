package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitDefinitionRequest {

    @NotBlank(message = "Bad request: name is blank")
    @NotNull(message = "Bad request: name is null")
    @Size(min = 3, max = 30, message = "Bad request: name must be between 3 and 30 characters long")
    String name;

    @NotBlank(message = "Bad request: description is blank")
    @NotNull(message = "Bad request: description is null")
    @Size(min = 3, max = 255, message = "Bad request: description must be between 3 and 255 characters long")
    String description;

    @NotNull(message = "Bad request: frequency is null")
    @Min(value = 0, message = "Bad request: frequency must be more or equal to 0")
    @Max(value = 365, message = "Bad request: frequency must be less or equal to 365")
    Integer frequency;

    @NotNull(message = "Bad request: allow recurring is null")
    Boolean allowRecurring;

    @NotNull(message = "Bad request: type id is null")
    Long typeId;

    @NotNull(message = "Bad request: city id is null")
    Long cityId;

}
