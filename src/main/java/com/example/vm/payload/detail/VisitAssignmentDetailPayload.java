package com.example.vm.payload.detail;

import com.example.vm.payload.list.CustomerListPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitAssignmentDetailPayload {

    private Timestamp createdTime;

    private Timestamp lastModifiedTime;

    private UUID uuid;

    private Date date;

    private String comment;

    private int enabled;

    private List<CustomerListPayload> customers;
}
