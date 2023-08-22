package com.example.vm.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;
@Data
@AllArgsConstructor

public class CustomersInAnAreaListPayload {

    private String city;

    private Double percentage;

}
