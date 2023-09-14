package com.example.vm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class ReceiptResponse {

    private CustomerResponse customer;
    private UserResponse user;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy | hh:mm:s")
    private Date date;
    private Double amount;
    private String paymentType;
    private String visitType;
}
