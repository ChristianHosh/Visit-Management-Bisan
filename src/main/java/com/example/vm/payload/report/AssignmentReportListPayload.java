package com.example.vm.payload.report;

import com.example.vm.model.VisitAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class AssignmentReportListPayload {

    private Long id;

    private String comment;

    private Date date;

    private String username;

    private String firstName;

    private String lastName;

    private List<CustomerReportListPayload> customer;

    public static List<AssignmentReportListPayload> toPayload(List<VisitAssignment> visitAssignmentList) {
        return visitAssignmentList.stream().map(VisitAssignment::toReportListPayload).toList();
    }

}
