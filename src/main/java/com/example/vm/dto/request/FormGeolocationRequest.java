package com.example.vm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormGeolocationRequest {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    private String note;
}
