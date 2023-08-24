package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRequest {

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

    @Min(value = 0)
    @Max(value = 1)
    Integer accessLevel;

}

