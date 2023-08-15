package com.example.vm.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class AssignmentCustomerDTO {

    @NotNull(message = "Invalid Assignment : uuid is NULL")
    private UUID assignmentId;

    @NotNull(message = "Invalid Customer : uuid is NULL")
    private UUID customerId;
}
