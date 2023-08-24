package com.example.vm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserPostRequest extends UserRequest {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    String username;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30)
    String password;

    @NotBlank
    @NotNull
    @Size(min = 8, max = 30)
    String confirmPassword;


    public UserPostRequest(final String firstName, final String lastName, final Integer accessLevel, final String username, final String password, final String confirmPassword) {
        super(firstName, lastName, accessLevel);
        this.username = username;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
