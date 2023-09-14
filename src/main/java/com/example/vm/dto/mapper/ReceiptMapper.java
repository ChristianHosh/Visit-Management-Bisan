package com.example.vm.dto.mapper;

import com.example.vm.dto.response.ReceiptResponse;
import com.example.vm.model.templates.PaymentReceipt;

import java.util.List;

public class ReceiptMapper {


    public static ReceiptResponse toResponse(PaymentReceipt paymentReceipt){
        return ReceiptResponse.builder()
                .customer(CustomerMapper.toListResponse(paymentReceipt.getVisitForm().getCustomer()))
                .user(UserMapper.toListResponse(paymentReceipt.getVisitForm().getVisitAssignment().getUser()))
                .amount(paymentReceipt.getAmount())
                .date(paymentReceipt.getCreatedTime())
                .paymentType(paymentReceipt.getPaymentType().name())
                .visitType(paymentReceipt.getVisitForm().getVisitAssignment().getVisitDefinition().getType().getName())
                .build();
    }

    public static List<ReceiptResponse> listToResponse(List<PaymentReceipt> paymentReceiptList){
        return paymentReceiptList.stream()
                .map(ReceiptMapper::toResponse)
                .toList();
    }
}
