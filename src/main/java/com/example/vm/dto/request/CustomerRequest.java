package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomerRequest {

    @NotNull
    @NotBlank
    @Size(min = 5, max = 30)
    private String name;

    @NotNull
    @NotBlank
    @Size(max = 50)
    private String addressLine1;

    @NotNull
    @Size(max = 50)
    private String addressLine2;

    @NotNull
    @NotBlank
    @Size(max = 5)
    private String zipcode;

    @NotNull
    @Min(-180)
    @Max(180)
    private Double longitude;

    @NotNull
    @Min(-90)
    @Max(90)
    private Double latitude;

    @NotNull
    private Long cityId;
}
