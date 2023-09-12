package com.example.vm.dto.request.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormPaymentRequest extends FormRequest {

    @NotNull(message = "Bad request: amount must not be null")
    private Double amount;

    @NotNull(message = "Bad request: type must not be null")
    private Integer type;

}
