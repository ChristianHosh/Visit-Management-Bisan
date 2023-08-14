package com.example.vm.service;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
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

        return ResponseEntity.ok(toDefinitionPayloadList(repository.findAll()));

    }

    public ResponseEntity<VisitDefinitionDetailPayload> findVisitDefinitionByUUID(UUID uuid) {
        VisitDefinition foundVisitDefinition = repository.findById(uuid)
                .orElseThrow( () -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));

        return ResponseEntity.ok(foundVisitDefinition.toDetailPayload());
    }

    public ResponseEntity<VisitDefinition>saveNewVisit(VisitDefinitionPostDTO VisitDefinitionRequest) {
        VisitType visitType=visitTypeRepository.findById(VisitDefinitionRequest.getTypeUUID())
                .orElseThrow( () -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));


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
        VisitDefinitionToSave.setVisitAssignments(new ArrayList<>());
        VisitDefinitionToSave= repository.save(VisitDefinitionToSave);
        return ResponseEntity.ok(VisitDefinitionToSave);
    }

    public ResponseEntity<VisitDefinitionDetailPayload> updateVisitDefinition(UUID id , VisitDefinitionPutDTO updatedDTO) {

        VisitDefinition VisitDefinitionToUpdate = repository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));
        VisitType updatedVisitType=visitTypeRepository.findById(updatedDTO.getTypeUUID())
                .orElseThrow( () -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));

        VisitDefinitionToUpdate.setName(updatedDTO.getName());
        VisitDefinitionToUpdate.setType(updatedVisitType);
        VisitDefinitionToUpdate.setDescription(updatedDTO.getDescription());
        VisitDefinitionToUpdate.setAllowRecurring(updatedDTO.getAllowRecurring());
        VisitDefinitionToUpdate =repository.save(VisitDefinitionToUpdate);
        return ResponseEntity.ok(VisitDefinitionToUpdate.toDetailPayload());
    }

    public VisitDefinition enableVisitDefinition(VisitDefinition visitDefinition) {
        visitDefinition.setEnabled(visitDefinition.getEnabled() == 0 ? 1 : 0);

        return repository.save(visitDefinition);
    }


    //TODO FIX SEARCH BY TYPE
//    public List<VisitDefinition> searchByType(int type) {
//        return repository.searchVisitDefinitionsByType(type);
//    }

    public ResponseEntity<List<VisitDefinitionListPayload>>searchByFrequency(int frequency) {
        return ResponseEntity.ok(toDefinitionPayloadList(repository.searchVisitDefinitionsByFrequency(frequency)));
    }


    public  ResponseEntity<List<VisitDefinitionListPayload>>searchByAllowRecurring(boolean allowRecurring){
        return ResponseEntity.ok(toDefinitionPayloadList(repository.searchVisitDefinitionsByAllowRecurring(allowRecurring)));

    }

    public  ResponseEntity<List<VisitDefinitionListPayload>> searchByName(String name) {
        return ResponseEntity.ok(toDefinitionPayloadList(repository.searchVisitDefinitionsByNameContaining(name)));

    }
    private static List<VisitDefinitionListPayload> toDefinitionPayloadList(List<VisitDefinition> visitDefinitionList) {
        return visitDefinitionList.stream().map(VisitDefinition::toListPayload).toList();
    }

    public ResponseEntity<VisitAssignmentDetailPayload> saveNewVisitAssignmentToDefinition(UUID id, VisitAssignmentPostDTO visitAssignmentRequest) {
        VisitDefinition visitDefinition= repository.findById(id)
                .orElseThrow( () -> new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND));
        VisitAssignment visitAssignment = VisitAssignment.builder()
                .comment(visitAssignmentRequest.getComment())
                .date(visitAssignmentRequest.getDate())
                .enabled(1)
                .build();
        visitDefinition.getVisitAssignments().add(visitAssignment);

        return ResponseEntity.ok(visitAssignment.toDetailPayload());
    }
}
