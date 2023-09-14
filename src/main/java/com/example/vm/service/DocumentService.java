package com.example.vm.service;

import com.example.vm.dto.mapper.ReceiptMapper;
import com.example.vm.dto.response.ReceiptResponse;
import com.example.vm.repository.PaymentReceiptRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class DocumentService {

    private final PaymentReceiptRepository paymentReceiptRepository;

    public DocumentService(PaymentReceiptRepository paymentReceiptRepository) {
        this.paymentReceiptRepository = paymentReceiptRepository;
    }


    public ResponseEntity<?> getAllReceipts() {
        List<ReceiptResponse> responseList = ReceiptMapper.listToResponse(paymentReceiptRepository.findAll());

        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<?> searchReceipts(Long customerId, String userUsername, String visitTypeName, String startStr, String endStr) {
        Date startDate = startStr == null ? null : Date.valueOf(startStr);
        Date endDate = endStr == null ? null : Date.valueOf(endStr);

        List<ReceiptResponse> responseList = ReceiptMapper
                .listToResponse(paymentReceiptRepository.searchReceipts(customerId, userUsername, visitTypeName, startDate, endDate));

        return ResponseEntity.ok(responseList);
    }
}
