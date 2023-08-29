package com.example.vm.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.util.List;

@Getter
@Setter
public class VisitAssignmentResponse extends ModelAuditResponse {

    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date date;
    private String comment;
    private VisitDefinitionResponse visitDefinition;
    private VisitTypeResponse visitType;
    private List<CustomerResponse> customers;
    private UserResponse user;

}
