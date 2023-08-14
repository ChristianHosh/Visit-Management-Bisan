package com.example.vm.payload.list;

import com.example.vm.model.visit.VisitAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitAssignmentListPayload {

    private UUID uuid;

    private Date date;

    private String comment;

    private int enabled;

    public static List<VisitAssignmentListPayload> toPayload(List<VisitAssignment> assignmentList) {
        return assignmentList.stream().map(VisitAssignment::toListPayload).toList();
    }
}
