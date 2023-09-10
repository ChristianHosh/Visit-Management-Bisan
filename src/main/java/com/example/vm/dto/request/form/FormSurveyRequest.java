package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormSurveyRequest extends FormRequest {

    @NotNull
    private String answer1;

    @NotNull
    private String answer2;

    @NotNull
    private String answer3;

}
