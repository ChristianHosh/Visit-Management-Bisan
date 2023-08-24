package com.example.vm.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;

public record CustomerRequest(

        @NotNull
        @NotBlank
        @Size(min = 5, max = 30)
        String name,

        @NotNull
        @NotBlank
        @Size(max = 50)
        String addressLine1,

        @NotNull
        @Size(max = 50)
        String addressLine2,

        @NotNull
        @NotBlank
        @Size(max = 5)
        String zipcode,

        @NotNull
        Boolean precise,

        @NotNull
        @Min(-180)
        @Max(180)
        Double longitude,

        @NotNull
        @Min(-90)
        @Max(90)
        Double latitude,

        @NotNull
        Long cityId

) {
}
