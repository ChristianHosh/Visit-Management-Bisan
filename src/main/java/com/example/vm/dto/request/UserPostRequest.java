package com.example.vm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserPostRequest extends UserRequest {

    @NotBlank(message = "Bad request: username is blank")
    @NotNull(message = "Bad request: username is null")
    @Size(min = 3, max = 30, message = "Bad request: username must be between 3 and 30 characters long")
    String username;

    @NotBlank(message = "Bad request: password is blank")
    @NotNull(message = "Bad request: password is null")
    @Size(min = 8, max = 30, message = "Bad request: password must be between 8 and 30 characters long")
    String password;

    @NotBlank(message = "Bad request: confirm password is blank")
    @NotNull(message = "Bad request: confirm password is null")
    @Size(min = 8, max = 30, message = "Bad request, confirm password must be between 8 and 30 characters long")
    String confirmPassword;


    public UserPostRequest(final String firstName, final String lastName, final Integer accessLevel, final String username, final String password, final String confirmPassword) {
        super(firstName, lastName, accessLevel);
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
