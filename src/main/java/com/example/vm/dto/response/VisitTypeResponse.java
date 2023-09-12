package com.example.vm.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitTypeResponse extends ModelAuditResponse {
    private Long id;
    private String name;
    private String base;
}
