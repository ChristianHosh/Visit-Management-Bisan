package com.example.vm.payload.detail;

import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
@Data
@AllArgsConstructor
public class VisitDefinitionDetailPayload {

    private Timestamp createdTime;

    private Timestamp lastModifiedTime;

    private UUID uuid;

    private String name;

    private String description;

    private VisitType type;

    private int frequency;

    private boolean allowRecurring;

    private int enabled;
    private List<VisitAssignmentListPayload> visitAssignments;


}
