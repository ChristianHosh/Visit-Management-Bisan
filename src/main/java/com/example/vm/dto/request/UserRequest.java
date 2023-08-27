package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Bad request: first name is blank")
    @NotNull(message = "Bad request: first name is null")
    @Size(min = 3, max = 30, message = "Bad request: first name must be between 3 and 30 characters long")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Bad request: first name must only contain letters")
    @Pattern(regexp = "^[A-Za-z\\s]*$")
    String firstName;

    @NotBlank(message = "Bad request: last name is blank")
    @NotNull(message = "Bad request: last name is null")
    @Size(min = 3, max = 30, message = "Bad request: last name must be between 3 and 30 characters long")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Bad request: last name must only contain letters")
    String lastName;

    @Min(value = 0, message = "Bad request: access level must be more or equal to 0")
    @Max(value = 1, message = "Bad request: access level must be less or equal to 1")
    Integer accessLevel;

}

