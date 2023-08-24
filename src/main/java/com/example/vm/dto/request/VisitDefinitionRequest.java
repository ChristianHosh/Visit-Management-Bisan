package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VisitDefinitionRequest {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    String name;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 255)
    String description;

    @NotNull
    @Min(value = 0)
    @Max(value = 365)
    Integer frequency;

    @NotNull
    Boolean allowRecurring;

    @NotNull
    Long typeId;

}
