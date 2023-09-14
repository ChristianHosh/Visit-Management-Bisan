package com.example.vm.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SurveyTemplateResponse {
    private String question1;

    private String question2;

    private String question3;

}
