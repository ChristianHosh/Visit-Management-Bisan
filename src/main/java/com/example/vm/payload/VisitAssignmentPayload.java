package com.example.vm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitAssignmentPayload {

    private UUID uuid;

    private Date date;

    private String comment;

    private int enabled;
}
