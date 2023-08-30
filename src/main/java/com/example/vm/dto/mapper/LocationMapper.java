package com.example.vm.dto.mapper;

import com.example.vm.dto.request.VisitTypeRequest;
import com.example.vm.dto.response.VisitTypeResponse;
import com.example.vm.model.VisitType;

import java.util.List;

public class LocationMapper {
    public static VisitTypeResponse toListResponse(VisitType visitType){
        VisitTypeResponse response = new VisitTypeResponse();

        response.setId(visitType.getId());
        response.setName(visitType.getName());

        response.setEnabled(visitType.getEnabled());
        response.setCreatedTime(visitType.getCreatedTime());
        response.setLastModifiedTime(visitType.getLastModifiedTime());

        return response;
    }

    public static List<VisitTypeResponse> listToResponseList(List<VisitType> visitTypeList){
        return visitTypeList
                .stream()
                .map(VisitTypeMapper::toListResponse)
                .toList();
    }

    public static VisitType toEntity(VisitTypeRequest typeRequest){
        return VisitType
                .builder()
                .name(typeRequest.getName())
                .build();
    }

}
