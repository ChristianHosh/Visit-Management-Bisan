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
    private Long totalForms;
    private Long notStartedForms;
    private Long undergoingForms;
    private Long canceledForms;
    private Long completedForms;

    // FORMS PERCENTAGES
    private Double notStartedFormsPer;
    private Double undergoingFormsPer;
    private Double canceledFormsPer;
    private Double completedFormsPer;

    // OTHERS
    private Double averageCompletionTime;
    private Long lateFormsCount;
}
