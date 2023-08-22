package com.example.vm.payload.report;

import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class UserAssignmentReportPayload {

    private Long id;

    private Date date;

    private String username;

    private String firstName;

    private String lastName;

    private String type;

    public static List<UserAssignmentReportPayload> toPayload(Customer customer) {
        return customer.getVisitAssignments().stream().map(VisitAssignment::toListPayloadReportCustomer).toList();
    }

}
