package com.example.vm.payload.report;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LabelYPayload {

  private String label;

  private Long y;
}
