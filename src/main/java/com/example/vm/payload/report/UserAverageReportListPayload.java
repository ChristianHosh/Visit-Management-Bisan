package com.example.vm.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAverageReportListPayload {

    private String username;

    private Double average;
}
