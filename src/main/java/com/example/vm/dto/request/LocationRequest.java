package com.example.vm.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LocationRequest {

    @NotBlank(message = "Bad request: address is blank")
    @NotNull(message = "Bad request: address is null")
    @Size(min = 3, max = 30, message = "Bad request: address must be between 3 and 30 characters long")
    String address;

}
