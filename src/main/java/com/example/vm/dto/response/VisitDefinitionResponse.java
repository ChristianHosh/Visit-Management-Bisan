package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VisitDefinitionResponse extends ModelAuditResponse{

    private Long id;
    private String name;
    private String description;
    private Integer frequency;
    private Boolean allowRecurring;

    private LocationResponse location;
    private VisitTypeResponse visitType;
    private List<VisitAssignmentResponse> visitAssignments;

}
