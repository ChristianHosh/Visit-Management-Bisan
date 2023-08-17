package com.example.vm.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor

public class CustomerReportListPayload {
    private UUID id;

    private String name;


}
