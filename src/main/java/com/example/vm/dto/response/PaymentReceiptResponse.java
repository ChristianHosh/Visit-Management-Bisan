package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentReceiptResponse {
    CustomerResponse customer;
    UserResponse user;
    Double amount;
    String paymentType;
}
