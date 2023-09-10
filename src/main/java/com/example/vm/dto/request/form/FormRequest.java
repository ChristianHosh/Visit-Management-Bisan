package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormRequest {
    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    private String note;
}
