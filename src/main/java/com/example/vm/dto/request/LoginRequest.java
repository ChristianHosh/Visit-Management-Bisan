package com.example.vm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank(message = "Bad request: username is blank")
    @NotNull(message = "Bad request: username is null")
    @Size(min = 3, max = 30, message = "Bad request: username must be between 3 and 30 characters long")
    String username;

    @NotBlank(message = "Bad request: password is blank")
    @NotNull(message = "Bad request: password is null")
    @Size(min = 8, max = 30, message = "Bad request: password must be between 8 and 30 characters long")
    String password;

}