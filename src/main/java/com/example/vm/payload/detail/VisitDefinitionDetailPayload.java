package com.example.vm.payload.detail;

import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;
@Data
@AllArgsConstructor
public class VisitDefinitionDetailPayload {

    private Timestamp createdTime;

    private Timestamp lastModifiedTime;

    private Long id;

    private String name;

    private String description;

    private VisitType type;

    private int frequency;

    private Boolean allowRecurring;

    private Boolean enabled;

    private List<VisitAssignmentListPayload> visitAssignments;


}
