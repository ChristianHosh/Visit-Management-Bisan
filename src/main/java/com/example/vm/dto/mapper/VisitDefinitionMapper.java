package com.example.vm.dto.mapper;

import com.example.vm.dto.response.VisitDefinitionResponse;
import com.example.vm.model.VisitDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VisitDefinitionMapper {

    public static VisitDefinitionResponse toListResponse(VisitDefinition definition) {
        if (definition == null) return null;

        return setBasicAttributes(definition);
    }

    public static VisitDefinitionResponse toDetailedResponse(VisitDefinition definition) {
        if (definition == null) return null;

        VisitDefinitionResponse response = setBasicAttributes(definition);

        response.setVisitAssignments(VisitAssignmentMapper.listToResponseList(definition.getVisitAssignments()));

        return response;
    }

    @NotNull
    private static VisitDefinitionResponse setBasicAttributes(VisitDefinition definition) {
        VisitDefinitionResponse response = new VisitDefinitionResponse();

        response.setId(definition.getId());
        response.setName(definition.getName());
        response.setDescription(definition.getDescription());
        response.setFrequency(definition.getFrequency());
        response.setAllowRecurring(definition.getAllowRecurring());
        response.setVisitType(VisitTypeMapper.toListResponse(definition.getType()));

        response.setEnabled(definition.getEnabled());
        response.setCreatedTime(definition.getCreatedTime());
        response.setLastModifiedTime(definition.getLastModifiedTime());

        if (definition.getCity() != null){
            response.setCityId(definition.getCity().getId());
            response.setCityName(definition.getCity().getName());
        }

        return response;
    }

    public static List<VisitDefinitionResponse> listToResponseList(List<VisitDefinition> visitDefinitionList) {
        return visitDefinitionList
                .stream()
                .map(VisitDefinitionMapper::toListResponse)
                .toList();
    }
}
