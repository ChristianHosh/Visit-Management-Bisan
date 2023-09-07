package com.example.vm.dto.report;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteractionResponse {

    private String customerName;
    private String customerAddress;
    private Double longitude;
    private Double latitude;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date formDueDate;
    private String formStatus;
    private String formType;
    private Long formDuration;

}
