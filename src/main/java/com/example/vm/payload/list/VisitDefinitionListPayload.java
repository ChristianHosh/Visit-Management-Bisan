package com.example.vm.payload.list;

import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VisitDefinitionListPayload {

    private Long id;

    private String name;

    private String description;

    private VisitType type;

    private int frequency;

    private boolean allowRecurring;

    private boolean enabled;

    public static List<VisitDefinitionListPayload> toPayload(List<VisitDefinition> visitDefinitionList) {
        return visitDefinitionList.stream().map(VisitDefinition::toListPayload).toList();
    }
}
