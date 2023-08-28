package com.example.vm.dto.response;

import com.example.vm.model.enums.VisitStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VisitFormResponse extends ModelAuditResponse {
    private Long id;
    private VisitStatus status;
    private CustomerResponse customer;
}
