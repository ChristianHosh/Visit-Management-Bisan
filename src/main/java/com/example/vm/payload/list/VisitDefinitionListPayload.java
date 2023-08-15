package com.example.vm.payload.list;

import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class VisitDefinitionListPayload {

    private UUID uuid;

    private String name;

    private String description;

    private VisitType type;

    private int frequency;

    private boolean allowRecurring;

    private int enabled;

    public static List<VisitDefinitionListPayload> toPayload(List<VisitDefinition> visitDefinitionList) {
        return visitDefinitionList.stream().map(VisitDefinition::toListPayload).toList();
    }
}
