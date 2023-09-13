package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormAnswerRequest extends FormRequest {

    @NotNull(message = "Bad request: answer1 is null")
    @NotBlank(message = "Bad request: answer1 is blank")
    @Size(max = 255, message = "Bad request: answer1 must be less than 255 characters")
    private String answer1;

    @NotNull(message = "Bad request: answer2 is null")
    @NotBlank(message = "Bad request: answer2 is blank")
    @Size(max = 255, message = "Bad request: answer2 must be less than 255 characters")
    private String answer2;

    @NotNull(message = "Bad request: answer3 is null")
    @NotBlank(message = "Bad request: answer3 is blank")
    @Size(max = 255, message = "Bad request: answer3 must be less than 255 characters")
    private String answer3;

}
