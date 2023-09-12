package com.example.vm.model.templates;

import com.example.vm.model.VisitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
    List<PaymentReceipt> findByVisitForm_VisitAssignment(VisitAssignment visitAssignment);
}