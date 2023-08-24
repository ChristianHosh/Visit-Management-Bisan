package com.example.vm.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class VisitTypeRequest {

    @NotBlank
    @NotNull
    @Size(min = 3, max = 30)
    @JsonProperty("name")
    private String name;

    @JsonCreator
    public VisitTypeRequest(String name) {
        this.name = name;
    }
}
