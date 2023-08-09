package com.example.vm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitAssignmentDetailedPayload {

    private UUID uuid;

    private Date date;

    private String comment;

    private int enabled;

    private List<CustomerPayload> customers;
}
