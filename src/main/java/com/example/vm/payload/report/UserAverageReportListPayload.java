package com.example.vm.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAverageReportListPayload {

    private String username;

    private String firstName;

    private String lastName;

    private Double average;
}
