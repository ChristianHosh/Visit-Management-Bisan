package com.example.vm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data

public class USERDTO {

    @NotNull(message = "Invalid UserName : UserName is NULL")
    private String username;
}
