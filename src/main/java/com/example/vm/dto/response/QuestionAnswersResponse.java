package com.example.vm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class QuestionAnswersResponse {

    private CustomerResponse customer;
    private UserResponse user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy | hh:mm:s")
    private Date date;
    private String answer1;
    private String answer2;
    private String answer3;
}
