package com.example.vm.dto.report;

import com.example.vm.dto.response.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPerformanceResponse {

    private UserResponse user;

    // ASSIGNMENTS COUNT
    private Long totalAssignments;
    private Long notStartedAssignments;
    private Long undergoingAssignments;
    private Long completedAssignments;

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
