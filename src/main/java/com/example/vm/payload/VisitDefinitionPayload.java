package com.example.vm.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitDefinitionPayload {

    private UUID uuid;

    private String name;

    private String description ;

    private int  type;

    private int  frequency;

    private boolean allowRecurring;

    private int enabled;
}
