package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerRequest {

    @NotNull(message = "Bad request: name is null")
    @NotBlank(message = "Bad request: name is blank")
    @Size(min = 5, max = 30, message = "Bad request: name must be between 5 and 30 characters long")
    private String name;

    @NotNull(message = "Bad request: longitude is null")
    @Min(value = -180, message = "Bad request: longitude must be more than (-180)")
    @Max(value = 180, message = "Bad request: longitude must be less than (180)")
    private Double longitude;

    @NotNull(message = "Bad request: latitude is null")
    @Min(value = -90, message = "Bad request: latitude must be more than (-90)")
    @Max(value = 90,message = "Bad request: latitude must be less than (90)")
    private Double latitude;

    @NotNull(message = "Bad request: location id is null")
    private Long locationId;
}
