package com.example.vm.dto.mapper;

import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.response.VisitAssignmentResponse;
import com.example.vm.model.User;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitDefinition;
import com.example.vm.model.enums.VisitStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
        response.setStatus(assignment.getStatus().toString());
        response.setUser(UserMapper.toListResponse(assignment.getUser()));
        response.setVisitType(VisitTypeMapper.toListResponse(assignment.getVisitDefinition().getType()));
        response.setLocation(LocationMapper.toResponse(assignment.getVisitDefinition().getLocation()));

        response.setEnabled(assignment.getEnabled());
        response.setCreatedTime(assignment.getCreatedTime());
        response.setLastModifiedTime(assignment.getLastModifiedTime());

        return response;
    }

    public static VisitAssignment toEntity(VisitAssignmentRequest assignmentRequest, VisitDefinition visitDefinition, User userToAssign) {
        return VisitAssignment
                .builder()
                .comment(assignmentRequest.getComment().trim())
                .date(assignmentRequest.getDate())
                .user(userToAssign)
                .customers(new ArrayList<>())
                .visitForms(new ArrayList<>())
                .status(VisitStatus.NOT_STARTED)
                .visitDefinition(visitDefinition)
                .build();
    }

    public static void update(VisitAssignment oldAssignment, VisitAssignmentRequest assignmentRequest) {
        oldAssignment.setComment(assignmentRequest.getComment());
        oldAssignment.setDate(assignmentRequest.getDate());
    }
}
