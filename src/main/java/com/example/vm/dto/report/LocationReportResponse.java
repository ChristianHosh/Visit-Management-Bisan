package com.example.vm.dto.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationReportResponse {

    private String cityName;
    private String locationName;
    private long newCustomers;
    private long totalCustomers;
    private long totalAssignments;
    private long notStartedAssignments;
    private long undergoingAssignments;
    private long completedAssignments;

}
