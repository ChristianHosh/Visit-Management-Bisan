package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitDefinitionService {
    private final VisitDefinitionRepository visitDefinitionRepository;
    private final VisitTypeRepository visitTypeRepository;

    @Autowired
    public VisitDefinitionService(VisitDefinitionRepository visitDefinitionRepository, VisitTypeRepository visitTypeRepository) {
        this.visitDefinitionRepository = visitDefinitionRepository;
        this.visitTypeRepository = visitTypeRepository;
    }

    public ResponseEntity<List<VisitDefinitionListPayload>> findAllVisitDefinition() {

        return ResponseEntity.ok(VisitDefinitionListPayload.toPayload(visitDefinitionRepository.findAll()));

    }

    public ResponseEntity<VisitDefinitionDetailPayload> findVisitDefinitionByUUID(Long id) {
        VisitDefinition foundVisitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.DEFINITION_NOT_FOUND));

        return ResponseEntity.ok(foundVisitDefinition.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisit(VisitDefinitionPostDTO VisitDefinitionRequest) {
        VisitType visitType = visitTypeRepository.findById(VisitDefinitionRequest.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.TYPE_NOT_FOUND));

        VisitDefinition visitDefinitionToSave;

        if (VisitDefinitionRequest.getAllowRecurring()) {
            visitDefinitionToSave = VisitDefinition.builder()
                    .name(VisitDefinitionRequest.getName())
                    .description(VisitDefinitionRequest.getDescription())
                    .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                    .frequency(VisitDefinitionRequest.getFrequency())
                    .type(visitType)
                    .enabled(1)
                    .build();
        } else {
            visitDefinitionToSave = VisitDefinition.builder()
                    .name(VisitDefinitionRequest.getName())
                    .description(VisitDefinitionRequest.getDescription())
                    .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                    .frequency(0)
                    .type(visitType)
                    .enabled(1)
                    .build();

        }

        visitDefinitionToSave.setVisitAssignments(new ArrayList<>());

        visitDefinitionToSave = visitDefinitionRepository.save(visitDefinitionToSave);

        return ResponseEntity.ok(visitDefinitionToSave.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> updateVisitDefinition(Long id, VisitDefinitionPutDTO updatedDTO) {

        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.DEFINITION_NOT_FOUND));

        VisitType foundVisitType = visitTypeRepository.findById(updatedDTO.getTypeID())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.TYPE_NOT_FOUND));

        foundDefinition.setName(updatedDTO.getName());
        foundDefinition.setType(foundVisitType);
        foundDefinition.setDescription(updatedDTO.getDescription());
        foundDefinition.setAllowRecurring(updatedDTO.getAllowRecurring());
        foundDefinition.setFrequency(updatedDTO.getFrequency());

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(foundDefinition.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> enableVisitDefinition(Long id) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.DEFINITION_NOT_FOUND));

        foundDefinition.setEnabled(foundDefinition.getEnabled() == 0 ? 1 : 0);

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(foundDefinition.toDetailPayload());

    }

    public ResponseEntity<List<VisitDefinitionListPayload>> searchByQuery(String query) {

        List<VisitDefinitionListPayload> result = new ArrayList<>();

        List<VisitDefinitionListPayload> list1 = VisitDefinitionListPayload.toPayload(visitDefinitionRepository.searchVisitDefinitionsByNameContaining(query));
        List<VisitDefinitionListPayload> list2 = new ArrayList<>();

        try {
            list2 = VisitDefinitionListPayload.toPayload(visitDefinitionRepository.searchVisitDefinitionsByFrequency(Integer.parseInt(query)));
        } catch (NumberFormatException ignored) {
        }

        result.addAll(list1);
        result.addAll(list2);

        return ResponseEntity.ok(result);

    }

    public ResponseEntity<List<VisitDefinitionListPayload>> searchByType(Long id) {
        VisitType foundType = visitTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.TYPE_NOT_FOUND));

        List<VisitDefinition> visitDefinitionList = visitDefinitionRepository.searchVisitDefinitionsByType(foundType);

        return ResponseEntity.ok(VisitDefinitionListPayload.toPayload(visitDefinitionList));
    }


    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisitAssignmentToDefinition(Long id, VisitAssignmentPostDTO visitAssignmentRequest) {

        VisitDefinition visitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.DEFINITION_NOT_FOUND));

        VisitAssignment visitAssignment = VisitAssignment.builder()
                .comment(visitAssignmentRequest.getComment())
                .date(visitAssignmentRequest.getDate())
                .enabled(1)
                .build();

        System.out.println("COMMENT : " + visitAssignment.getComment());

        visitAssignment.setVisitDefinition(visitDefinition);
        visitDefinition.getVisitAssignments().add(visitAssignment);

        visitDefinition = visitDefinitionRepository.save(visitDefinition);

        System.out.println("VISIT DEFINITION : " + visitDefinition.toDetailPayload());

        return ResponseEntity.status(HttpStatus.CREATED).body(visitDefinition.toDetailPayload());
    }
}
