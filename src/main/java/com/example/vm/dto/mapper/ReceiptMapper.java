package com.example.vm.dto.mapper;

import com.example.vm.dto.response.PaymentReceiptResponse;
import com.example.vm.model.templates.PaymentReceipt;

import java.util.List;

public class ReceiptMapper {


    public static PaymentReceiptResponse toResponse(PaymentReceipt paymentReceipt){
        PaymentReceiptResponse response = new PaymentReceiptResponse();

        response.setCustomer(CustomerMapper.toListResponse(paymentReceipt.getVisitForm().getCustomer()));
        response.setUser(UserMapper.toListResponse(paymentReceipt.getVisitForm().getVisitAssignment().getUser()));
        response.setAmount(paymentReceipt.getAmount());
        response.setPaymentType(paymentReceipt.getPaymentType().name());

        return response;
    }

    public static List<PaymentReceiptResponse> listToResponse(List<PaymentReceipt> paymentReceiptList){
        return paymentReceiptList.stream()
                .map(ReceiptMapper::toResponse)
                .toList();
    }
}
