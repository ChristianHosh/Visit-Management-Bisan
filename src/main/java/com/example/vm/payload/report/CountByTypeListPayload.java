package com.example.vm.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CountByTypeListPayload {
    private String name;

    private double percentage;


    public CountByTypeListPayload(String name, double percentage) {
        this.name=name;
        this.percentage=percentage;
    }
}
