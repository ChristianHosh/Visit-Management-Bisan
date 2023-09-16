package com.example.vm.dto.report;

import com.example.vm.dto.response.CustomerResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerPerformanceResponse {

    private CustomerResponse customer;

    // FORMS COUNT
    private long totalForms;
    private long notStartedForms;
    private long undergoingForms;
    private long canceledForms;
    private long completedForms;

    // FORMS PERCENTAGES
    private double notStartedFormsPer;
    private double undergoingFormsPer;
    private double canceledFormsPer;
    private double completedFormsPer;

    // OTHERS
    private double averageCompletionTime;
    private long lateFormsCount;
}
