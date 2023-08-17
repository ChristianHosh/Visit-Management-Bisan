package com.example.vm.payload.report;

import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitAssignment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class AssignmentCustomerReportListPayload {

    private UUID id;

    private Date date;

    private String username;

    private String firstName;

    private String lastName;

    private String  type;
    public static List<AssignmentCustomerReportListPayload> topayLoad (Customer customer){
        return customer.getVisitAssignments().stream().map(VisitAssignment::toListPayloadReportCustomer).toList();
    }

}
