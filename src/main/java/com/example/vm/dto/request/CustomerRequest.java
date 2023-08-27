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

    @NotNull(message = "Bad request: address line 1 is  null")
    @NotBlank(message = "Bad request: address line 1 is blank")
    @Size(max = 50, message = "Bad request: address line 1 must be less than 50 characters long")
    private String addressLine1;

    @NotNull(message = "Bad request: address line 2 is null")
    @Size(max = 50, message = "Bad request: address line 2 must be less than 50 characters long")
    private String addressLine2;

    @NotNull(message = "Bad request: zipcode is null")
    @NotBlank(message = "Bad request: zipcode is blank")
    @Size(max = 5, message = "Bad request: zipcode must be less than 5 characters long")
    private String zipcode;

    @NotNull(message = "Bad request: longitude is null")
    @Min(value = -180, message = "Bad request: longitude must be more than (-180)")
    @Max(value = 180, message = "Bad request: longitude must be less than (180)")
    private Double longitude;

    @NotNull(message = "Bad request: latitude is null")
    @Min(value = -90, message = "Bad request: latitude must be more than (-90)")
    @Max(value = 90,message = "Bad request: latitude must be less than (90)")
    private Double latitude;

    @NotNull(message = "Bad request: city id is null")
    private Long cityId;
}
