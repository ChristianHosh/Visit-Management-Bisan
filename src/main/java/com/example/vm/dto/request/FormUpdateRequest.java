package com.example.vm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormUpdateRequest {

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    private String note;

    private String answer1;

    private String answer2;

    private String answer3;

}
