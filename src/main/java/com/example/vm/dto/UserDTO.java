package com.example.vm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data

public class UserDTO {

    @NotNull(message = "Invalid Username : Username is NULL")
    private String username;
}
