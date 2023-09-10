package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormSurveyRequest extends FormRequest {

    private String answer1;

    private String answer2;

    private String answer3;

}
