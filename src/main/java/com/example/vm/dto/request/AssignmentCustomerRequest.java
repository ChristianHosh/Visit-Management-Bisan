package com.example.vm.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentCustomerRequest {

    @NotNull(message = "Bad request: assignment id is null")
    private Long assignmentId;

    @NotNull(message = "Bad request: customer id is null")
    private Long customerId;
}
