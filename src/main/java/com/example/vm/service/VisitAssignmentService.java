package com.example.vm.service;

import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.repository.VisitAssignmentRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VisitAssignmentService {
    private final VisitAssignmentRepository repository;

    public VisitAssignmentService(VisitAssignmentRepository repository) {
        this.repository = repository;
    }
    public List<VisitAssignment>findVisitAssignment() {
        List<VisitAssignment> optional = repository.findAll();
        return optional;
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
}
