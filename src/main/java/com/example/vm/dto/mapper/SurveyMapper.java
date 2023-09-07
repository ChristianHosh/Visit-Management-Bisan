package com.example.vm.dto.mapper;

import com.example.vm.dto.response.SurveyTemplateResponse;
import com.example.vm.model.templates.SurveyTemplate;

public class SurveyMapper {

    public static SurveyTemplateResponse toResponse(SurveyTemplate template){
        SurveyTemplateResponse response = new SurveyTemplateResponse();
        response.setQuestion1(template.getQuestion1());
        response.setQuestion2(template.getQuestion2());
        response.setQuestion3(template.getQuestion3());
        return response;
    }
}
