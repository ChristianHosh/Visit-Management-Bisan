package com.example.vm.payload.list;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitAssignmentListPayload {

    private UUID uuid;

    private Date date;

    private String comment;

    private int enabled;
}
