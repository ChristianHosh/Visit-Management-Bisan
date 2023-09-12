package com.example.vm.repository;

import com.example.vm.model.VisitAssignment;
import com.example.vm.model.templates.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
    List<PaymentReceipt> findByVisitForm_VisitAssignment(VisitAssignment visitAssignment);
}