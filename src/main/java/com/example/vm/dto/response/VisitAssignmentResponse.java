package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class VisitAssignmentResponse extends ModelAuditResponse {

    private Long id;
    private Date date;
    private String comment;
    private VisitDefinitionResponse visitDefinition;
    private List<CustomerResponse> customers;
    private UserResponse user;

//    private VisitAssignmentResponse nextAssignment;
}
