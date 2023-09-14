package com.example.vm.repository;

import com.example.vm.model.VisitAssignment;
import com.example.vm.model.templates.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.util.List;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
    List<PaymentReceipt> findByVisitForm_VisitAssignment(VisitAssignment visitAssignment);

    @Query("SELECT r FROM PaymentReceipt r " +
            "WHERE (" +
            "   (:customerId IS NULL OR r.visitForm.customer.id = :customerId) AND " +
            "   (:userUsername IS NULL OR r.visitForm.visitAssignment.user.username = :userUsername) AND " +
            "   (:visitTypeName IS NULL OR r.visitForm.visitAssignment.visitDefinition.type.name = :visitTypeName)  AND " +
            "   ((:startDate IS NULL AND :endDate IS NULL) OR r.createdTime BETWEEN :startDate AND :endDate)" +
            ") ")
    List<PaymentReceipt> searchReceipts(Long customerId, String userUsername, String visitTypeName, Date startDate, Date endDate);
}