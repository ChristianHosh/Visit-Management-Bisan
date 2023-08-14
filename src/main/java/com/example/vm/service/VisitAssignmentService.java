package com.example.vm.service;

import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.repository.VisitAssignmentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisitAssignmentService {
    private final VisitAssignmentRepository repository;

    public VisitAssignmentService(VisitAssignmentRepository repository) {
        this.repository = repository;
    }

    public List<VisitAssignment> findAllVisitAssignments() {
        return repository.findAll();
    }

    public VisitAssignment findVisitAssignmentByUUID(UUID uuid) {
        Optional<VisitAssignment> optional = repository.findById(uuid);
        return optional.orElse(null);
    }
    public VisitAssignment saveNewVisitAssignment(VisitDefinition visitDefinition, VisitAssignmentPostDTO visitAssignmentRequest) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        VisitAssignment visitAssignmentToSave = VisitAssignment.builder()
                .comment(visitAssignmentRequest.getComment())
                .date(visitAssignmentRequest.getDate())
                .enabled(1)
                .build();

        visitAssignmentToSave.setCreatedTime(timestamp);
        visitAssignmentToSave.setLastModifiedTime(timestamp);

        visitAssignmentToSave.setVisitDefinition(visitDefinition);

        return repository.save(visitAssignmentToSave);
    }
    public VisitAssignment updateVisitAssignment(VisitAssignment VisitAssignmentToUpdate, VisitAssignmentPutDTO updatedDTO) {
        VisitAssignmentToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));
        VisitAssignmentToUpdate.setComment(updatedDTO.getComment());
        VisitAssignmentToUpdate.setDate(updatedDTO.getDate());
        return repository.save(VisitAssignmentToUpdate);
    }
    public VisitAssignment enableVisitAssignment(VisitAssignment visitAssignment) {
        visitAssignment.setEnabled(visitAssignment.getEnabled() == 0 ? 1 : 0);

        return repository.save(visitAssignment);
    }

    public VisitAssignment assignVisitToCustomer(VisitAssignment visitAssignment, Customer customer) {
        List<Customer> assignmentCustomers = visitAssignment.getCustomers();

        if (assignmentCustomers == null)
            assignmentCustomers = new ArrayList<>();

        assignmentCustomers.add(customer);

        visitAssignment.setCustomers(assignmentCustomers);

        return repository.save(visitAssignment);
    }

    public VisitAssignment assignVisitTouser(VisitAssignment visitAssignment, User user) {

        visitAssignment.setUser(user);
        return repository.save(visitAssignment);
    }
}

