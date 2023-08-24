package com.example.vm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public class UserPostRequest extends UserRequest {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30, message = "Invalid username: Must be of 3 - 30 characters")
    String username;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30, message = "Invalid password: Must be of 8 - 30 characters")
    String password;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30, message = "Invalid confirm password: Must be of 8 - 30 characters")
    String confirmPassword;
}
