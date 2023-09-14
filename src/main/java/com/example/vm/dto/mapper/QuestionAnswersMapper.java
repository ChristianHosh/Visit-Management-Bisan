package com.example.vm.dto.mapper;

import com.example.vm.dto.response.QuestionAnswersResponse;
import com.example.vm.model.templates.QuestionAnswers;

import java.util.List;

public class QuestionAnswersMapper {

    public static QuestionAnswersResponse toResponse(QuestionAnswers q){
        return QuestionAnswersResponse.builder()
                .answer1(q.getAnswer1())
                .answer2(q.getAnswer2())
                .answer3(q.getAnswer3())
                .date(q.getCreatedTime())
                .customer(CustomerMapper.toListResponse(q.getVisitForm().getCustomer()))
                .build();
    }

    public static List<QuestionAnswersResponse> listToResponseList(List<QuestionAnswers> list){
        return list.stream()
                .map(QuestionAnswersMapper::toResponse)
                .toList();
    }
}
