package com.example.vm.dto.mapper;

import com.example.vm.dto.request.VisitDefinitionRequest;
import com.example.vm.dto.response.VisitDefinitionResponse;
import com.example.vm.model.City;
import com.example.vm.model.VisitDefinition;
import com.example.vm.model.VisitType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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

        if (definition.getCity() != null) {
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

    public static VisitDefinition toEntity(VisitDefinitionRequest definitionRequest, VisitType type, City city) {
        return VisitDefinition
                .builder()
                .name(definitionRequest.getName())
                .description(definitionRequest.getDescription())
                .visitAssignments(new ArrayList<>())
                .type(type)
                .city(city)
                .allowRecurring(definitionRequest.getAllowRecurring())
                .frequency(definitionRequest.getAllowRecurring() ? definitionRequest.getFrequency() : 0)
                .build();
    }

    public static void update(VisitDefinition oldDefinition, VisitDefinitionRequest definitionRequest, VisitType type, City city) {
        oldDefinition.setName(definitionRequest.getName());
        oldDefinition.setDescription(definitionRequest.getDescription());
        oldDefinition.setType(type);
        oldDefinition.setCity(city);
        oldDefinition.setAllowRecurring(definitionRequest.getAllowRecurring());
        oldDefinition.setFrequency(definitionRequest.getAllowRecurring() ? definitionRequest.getFrequency() : 0);
    }
}
