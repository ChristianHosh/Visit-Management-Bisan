package com.example.vm.payload.list;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitDefinitionListPayload {

    private UUID uuid;

    private String name;

    private String description ;

    private int  type;

    private int  frequency;

    private boolean allowRecurring;

    private int enabled;
}
