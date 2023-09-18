package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FormAnswerRequest extends FormRequest {

    @NotNull(message = "Bad request: answers is null")
    private List<
            @NotNull(message = "Bad request: answer is null")
            @NotBlank(message = "Bad request: answer is blank")
            @Size(max = 255, message = "Bad request: answer must be less than 255 characters")
                    String> answers;

}
