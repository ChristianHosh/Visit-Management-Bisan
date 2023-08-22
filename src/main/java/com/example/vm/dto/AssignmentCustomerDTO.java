package com.example.vm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignmentCustomerDTO {

    @NotNull(message = "Invalid Assignment : uuid is NULL")
    private Long assignmentId;

    @NotNull(message = "Invalid Customer : uuid is NULL")
    private Long customerId;
}
