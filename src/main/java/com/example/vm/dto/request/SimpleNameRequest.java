package com.example.vm.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SimpleNameRequest {

    @NotBlank(message = "Bad request: name is blank")
    @NotNull(message = "Bad request: name is null")
    @Size(min = 3, max = 30, message = "Bad request: name must be between 3 and 30 characters long")
    @JsonProperty("name")
    private String name;

    private Integer i;

    @JsonCreator
    public SimpleNameRequest(String name, Integer i) {
        this.name = name;
        this.i = i;
    }
}
