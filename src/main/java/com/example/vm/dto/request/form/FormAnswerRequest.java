package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FormAnswerRequest extends FormRequest {

    @NotNull(message = "Bad request: answers is null")
    private List<String> answers;

}
