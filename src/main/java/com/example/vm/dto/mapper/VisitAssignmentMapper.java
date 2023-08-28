package com.example.vm.dto.mapper;

import com.example.vm.dto.response.VisitAssignmentResponse;
import com.example.vm.model.VisitAssignment;

import java.util.List;

public class VisitAssignmentMapper {

    public static VisitAssignmentResponse toDetailedResponse(VisitAssignment assignment) {
        if (assignment == null) return null;

        VisitAssignmentResponse response = new VisitAssignmentResponse();

        response.setId(assignment.getId());
        response.setDate(assignment.getDate());
        response.setComment(assignment.getComment());
        response.setUser(UserMapper.toListResponse(assignment.getUser()));
        response.setCustomers(CustomerMapper.listToResponseList(assignment.getCustomers()));

        response.setEnabled(assignment.getEnabled());
        response.setCreatedTime(assignment.getCreatedTime());
        response.setLastModifiedTime(assignment.getLastModifiedTime());


        return response;
    }


    public static VisitAssignmentResponse toListResponse(VisitAssignment assignment) {
        if (assignment == null) return null;

        VisitAssignmentResponse response = new VisitAssignmentResponse();

        response.setId(assignment.getId());
        response.setDate(assignment.getDate());
        response.setComment(assignment.getComment());
        response.setUser(UserMapper.toListResponse(assignment.getUser()));

        response.setEnabled(assignment.getEnabled());
        response.setCreatedTime(assignment.getCreatedTime());
        response.setLastModifiedTime(assignment.getLastModifiedTime());

        return response;
    }

    public static List<VisitAssignmentResponse> listToResponseList(List<VisitAssignment> assignmentList) {
        if (assignmentList == null) return null;

        return assignmentList
                .stream()
                .map(VisitAssignmentMapper::toListResponse)
                .toList();
    }
}
