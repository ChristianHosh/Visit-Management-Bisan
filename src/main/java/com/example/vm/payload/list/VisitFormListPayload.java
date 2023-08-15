package com.example.vm.payload.list;

import com.example.vm.model.Customer;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.UUID;
@Data
@AllArgsConstructor
public class VisitFormListPayload {
    private UUID uuid;

    private Timestamp startTime;

    private Timestamp endTime;

    private VisitStatus status;

    private String note;

    private Double longitude;

    private Double latitude;


    private CustomerListPayload customer;


    private VisitAssignmentListPayload visitAssignment;
}
