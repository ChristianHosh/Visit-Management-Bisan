package com.example.vm.repository;

import com.example.vm.model.VisitAssignment;
import com.example.vm.model.templates.PaymentReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PaymentReceiptRepository extends JpaRepository<PaymentReceipt, Long> {
    @Query("select count(p) from PaymentReceipt p where p.createdTime = ?1")
    long countAmountByDate(Timestamp createdTime);
    @Query("SELECT SUM (p.amount) FROM PaymentReceipt p " +
            "WHERE p.createdTime > :date")
    double countAmountByCreatedAfter(Date date);
    @Query("SELECT SUM (p.amount) FROM PaymentReceipt p " +
            "WHERE p.createdTime BETWEEN :start AND :end")
    Optional<Long> countAmountByCreatedBetween(Timestamp start, Timestamp end);

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