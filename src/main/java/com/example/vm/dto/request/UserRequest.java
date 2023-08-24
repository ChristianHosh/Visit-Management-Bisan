package com.example.vm.dto.request;

import jakarta.validation.constraints.*;

import java.sql.Timestamp;

public record UserRequest(

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30, message = "Invalid username: Must be of 3 - 30 characters")
    String username,

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30, message = "Invalid password: Must be of 8 - 30 characters")
    String password,

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30, message = "Invalid confirm password: Must be of 8 - 30 characters")
    String confirmPassword,

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30, message = "Invalid firstName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid firstName : Must only contain characters")
    String firstName,

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30, message = "Invalid lastName: Must be of 3 - 30 characters")
    @Pattern(regexp = "^[A-Za-z\\s]*$", message = "Invalid lastName : Must only contain characters")
    String lastName,

    @Min(value = 0, message = "Invalid accessLevel: Equals to zero")
    @Max(value = 1, message = "Invalid accessLevel: Exceeds one")
    Integer accessLevel

){
}
