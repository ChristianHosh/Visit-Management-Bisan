package com.example.vm.dto.mapper;

import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.response.VisitAssignmentResponse;
import com.example.vm.model.VisitAssignment;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VisitAssignmentMapper {

    public static VisitAssignmentResponse toDetailedResponse(VisitAssignment assignment) {
        if (assignment == null) return null;

        VisitAssignmentResponse response = setBasicAttributes(assignment);

        response.setCustomers(CustomerMapper.listToResponseList(assignment.getCustomers()));

        return response;
    }

    public static VisitAssignmentResponse toListResponse(VisitAssignment assignment) {
        if (assignment == null) return null;

        return setBasicAttributes(assignment);
    }

    public static List<VisitAssignmentResponse> listToResponseList(List<VisitAssignment> assignmentList) {
        if (assignmentList == null) return null;

        return assignmentList
                .stream()
                .map(VisitAssignmentMapper::toListResponse)
                .toList();
    }

    @NotNull
    private static VisitAssignmentResponse setBasicAttributes(VisitAssignment assignment) {
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

    public static VisitAssignment toEntity(VisitAssignmentRequest assignmentRequest) {
        return VisitAssignment
                .builder()
                .comment(assignmentRequest.getComment())
                .date(assignmentRequest.getDate())
                .build();
    }

    public static VisitAssignment toEntity(VisitAssignment oldAssignment, VisitAssignmentRequest assignmentRequest) {
        oldAssignment.setComment(assignmentRequest.getComment());
        oldAssignment.setDate(assignmentRequest.getDate());

        return oldAssignment;
    }
}
