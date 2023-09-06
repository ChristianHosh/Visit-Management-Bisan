package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UnplannedVisitRequest {

    @NotNull(message = "Bad request: name is null")
    @NotBlank(message = "Bad request: name is blank")
    @Size(min = 5, max = 30, message = "Bad request: name must be between 5 and 30 characters long")
    String name;

    @NotNull(message = "Bad request: longitude is null")
    @Min(value = -180, message = "Bad request: longitude must be more than (-180)")
    @Max(value = 180, message = "Bad request: longitude must be less than (180)")
    Double longitude;

    @NotNull(message = "Bad request: latitude is null")
    @Min(value = -90, message = "Bad request: latitude must be more than (-90)")
    @Max(value = 90,message = "Bad request: latitude must be less than (90)")
    Double latitude;

    @NotBlank(message = "Bad request: first name is blank")
    @NotNull(message = "Bad request: first name is null")
    @Size(min = 3, max = 30, message = "Bad request: first name must be between 3 and 30 characters long")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Bad request: first name must only contain letters")
    String firstName;

    @NotBlank(message = "Bad request: last name is blank")
    @NotNull(message = "Bad request: last name is null")
    @Size(min = 3, max = 30, message = "Bad request: last name must be between 3 and 30 characters long")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Bad request: last name must only contain letters")
    String lastName;

    @NotBlank(message = "Bad request: phone number is blank")
    @NotNull(message = "Bad request: phone number is null")
    @Size(min = 10, max = 10, message = "Bad request: phone number must be 10 characters long")
    @Pattern(regexp = "^[0-9\\s]*$", message = "Bad request: phone number must only contain numbers")
    String phoneNumber;

    @NotNull(message = "Bad request: email is null")
    @Email(message = "Bad request: email is invalid")
    String email;
}
