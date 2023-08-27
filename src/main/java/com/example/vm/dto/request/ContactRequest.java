package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContactRequest {

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

    @NotBlank(message = "Bad request: email is blank")
    @NotNull(message = "Bad request: email is null")
    @Email(message = "Bad request: email is invalid")
    String email;

    @NotNull(message = "Bad request: types are null")
    List<Long> types;
}
