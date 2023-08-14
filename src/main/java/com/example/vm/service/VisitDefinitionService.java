package com.example.vm.service;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitDefinitionDetailPayload;
import com.example.vm.payload.list.VisitDefinitionListPayload;
import com.example.vm.repository.VisitDefinitionRepository;
import com.example.vm.repository.VisitTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class VisitDefinitionService {
    private final VisitDefinitionRepository repository;
    private final VisitTypeRepository visitTypeRepository;

    @Autowired
    public VisitDefinitionService(VisitDefinitionRepository repository, VisitTypeRepository visitTypeRepository) {
        this.repository = repository;
        this.visitTypeRepository = visitTypeRepository;
    }

    public ResponseEntity<List<VisitDefinitionListPayload>> findAllVisitDefinition() {

        return ResponseEntity.ok(VisitDefinitionListPayload.toPayload(repository.findAll()));

    }

    public ResponseEntity<VisitDefinitionDetailPayload> findVisitDefinitionByUUID(UUID uuid) {
        VisitDefinition foundVisitDefinition = repository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));

        return ResponseEntity.ok(foundVisitDefinition.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisit(VisitDefinitionPostDTO VisitDefinitionRequest) {
        VisitType visitType = visitTypeRepository.findById(VisitDefinitionRequest.getTypeUUID())
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.TYPE_NOT_FOUND));

        Timestamp timestamp = Timestamp.from(Instant.now());

        VisitDefinition VisitDefinitionToSave;

        if (VisitDefinitionRequest.getAllowRecurring()) {
            VisitDefinitionToSave = VisitDefinition.builder()
                    .name(VisitDefinitionRequest.getName())
                    .description(VisitDefinitionRequest.getDescription())
                    .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                    .frequency(VisitDefinitionRequest.getFrequency())
                    .type(visitType)
                    .enabled(1)
                    .build();
        } else {
            VisitDefinitionToSave = VisitDefinition.builder()
                    .name(VisitDefinitionRequest.getName())
                    .description(VisitDefinitionRequest.getDescription())
                    .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                    .frequency(0)
                    .type(visitType)
                    .enabled(1)
                    .build();

        }

        VisitDefinitionToSave.setCreatedTime(timestamp);
        VisitDefinitionToSave.setLastModifiedTime(timestamp);

        VisitDefinitionToSave = repository.save(VisitDefinitionToSave);

        return ResponseEntity.ok(VisitDefinitionToSave.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> updateVisitDefinition(UUID id, VisitDefinitionPutDTO updatedDTO) {

        VisitDefinition foundDefinition = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));

        VisitType foundVisitType = visitTypeRepository.findById(updatedDTO.getTypeUUID())
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.TYPE_NOT_FOUND));

        foundDefinition.setName(updatedDTO.getName());
        foundDefinition.setType(foundVisitType);
        foundDefinition.setDescription(updatedDTO.getDescription());
        foundDefinition.setAllowRecurring(updatedDTO.getAllowRecurring());
        foundDefinition.setFrequency(updatedDTO.getFrequency());

        foundDefinition.setLastModifiedTime(Timestamp.from(Instant.now()));

        foundDefinition = repository.save(foundDefinition);

        return ResponseEntity.ok(foundDefinition.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> enableVisitDefinition(UUID id) {
        VisitDefinition foundDefinition = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));

        foundDefinition.setEnabled(foundDefinition.getEnabled() == 0 ? 1 : 0);

        foundDefinition = repository.save(foundDefinition);

        return ResponseEntity.ok(foundDefinition.toDetailPayload());

    }

    public ResponseEntity<List<VisitDefinitionListPayload>> searchByQuery(String query) {

        List<VisitDefinitionListPayload> result = new ArrayList<>();

        List<VisitDefinitionListPayload> list1 = VisitDefinitionListPayload.toPayload(repository.searchVisitDefinitionsByNameContaining(query));
        List<VisitDefinitionListPayload> list2 = new ArrayList<>();

        try {
            list2 = VisitDefinitionListPayload.toPayload(repository.searchVisitDefinitionsByFrequency(Integer.parseInt(query)));
        } catch (NumberFormatException ignored) {
        }

        result.addAll(list1);
        result.addAll(list2);

        return ResponseEntity.ok(result);

    }


    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisitAssignmentToDefinition(UUID id, VisitAssignmentPostDTO visitAssignmentRequest) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        VisitDefinition visitDefinition = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));

        VisitAssignment visitAssignment = VisitAssignment.builder()
                .comment(visitAssignmentRequest.getComment())
                .date(visitAssignmentRequest.getDate())
                .enabled(1)
                .build();

        visitAssignment.setCreatedTime(timestamp);
        visitAssignment.setLastModifiedTime(timestamp);

        visitDefinition.getVisitAssignments().add(visitAssignment);

        visitDefinition = repository.save(visitDefinition);

        return ResponseEntity.ok(visitDefinition.toDetailPayload());
    }
}
