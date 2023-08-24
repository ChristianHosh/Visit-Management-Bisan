package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ContactRequest {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-z\\s]*$")
    String firstName;

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    @Pattern(regexp = "^[A-Za-z\\s]*$")
    String lastName;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^[0-9\\s]*$")
    String phoneNumber;

    @NotBlank
    @NotNull
    @Email
    String email;

    @NotNull
    List<Long> types;
}
