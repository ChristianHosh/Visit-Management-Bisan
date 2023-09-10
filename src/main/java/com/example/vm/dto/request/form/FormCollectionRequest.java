package com.example.vm.dto.request.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormCollectionRequest extends FormRequest {

    private Double amount;

    private Integer type;


}
