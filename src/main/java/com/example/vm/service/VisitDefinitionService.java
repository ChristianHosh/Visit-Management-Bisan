package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidDateException;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.request.VisitDefinitionRequest;
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

import java.util.*;

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

    public ResponseEntity<List<VisitDefinitionListPayload>> findAllEnabledVisitDefinitions() {
        return ResponseEntity.ok(VisitDefinitionListPayload.toPayload(visitDefinitionRepository.findVisitDefinitionsByEnabledTrue()));
    }

    public ResponseEntity<VisitDefinitionDetailPayload> findVisitDefinitionByID(Long id) {
        VisitDefinition foundVisitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));
        return ResponseEntity.ok(foundVisitDefinition.toDetailPayload());

    }

    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisit(VisitDefinitionRequest VisitDefinitionRequest) {
        VisitType visitType = visitTypeRepository.findByIdAndEnabledTrue(VisitDefinitionRequest.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        VisitDefinition visitDefinitionToSave = VisitDefinition.builder()
                .name(VisitDefinitionRequest.getName())
                .description(VisitDefinitionRequest.getDescription())
                .visitAssignments(new ArrayList<>())
                .type(visitType)
                .allowRecurring(VisitDefinitionRequest.getAllowRecurring())
                .frequency(VisitDefinitionRequest.getAllowRecurring() ? VisitDefinitionRequest.getFrequency() : 0)
                .build();

        visitDefinitionToSave = visitDefinitionRepository.save(visitDefinitionToSave);

        return ResponseEntity.ok(visitDefinitionToSave.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> updateVisitDefinition(Long id, VisitDefinitionRequest updatedDTO) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        VisitType foundVisitType = visitTypeRepository.findByIdAndEnabledTrue(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        foundDefinition.setName(updatedDTO.getName());
        foundDefinition.setDescription(updatedDTO.getDescription());
        foundDefinition.setType(foundVisitType);
        foundDefinition.setAllowRecurring(updatedDTO.getAllowRecurring());
        foundDefinition.setFrequency(updatedDTO.getAllowRecurring() ? updatedDTO.getFrequency() : 0);

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(foundDefinition.toDetailPayload());
    }

    public ResponseEntity<VisitDefinitionDetailPayload> enableVisitDefinition(Long id) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        foundDefinition.setEnabled(!foundDefinition.getEnabled());

        boolean isEnabled = foundDefinition.getEnabled();

        foundDefinition.getVisitAssignments()
                .forEach(visitAssignment -> visitAssignment.setEnabled(isEnabled));

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
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        List<VisitDefinition> visitDefinitionList = visitDefinitionRepository.searchVisitDefinitionsByType(foundType);

        return ResponseEntity.ok(VisitDefinitionListPayload.toPayload(visitDefinitionList));
    }

    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisitAssignmentToDefinition(Long id, VisitAssignmentRequest visitAssignmentRequest) {

        VisitDefinition visitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        // FIND DATE MAKE SURE IT HAS NOT REACHED THAT DATE
        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, -1);
        todayDate.setTime(calendar.getTime().getTime());

        // VALIDATES THE DATE TO MAKE SURE IT IN THE PRESENT OR FUTURE
        if (visitAssignmentRequest.getDate().before(todayDate))
            throw new InvalidDateException(ErrorMessage.DATE_IN_PAST);

        VisitAssignment visitAssignment = VisitAssignment.builder()
                .comment(visitAssignmentRequest.getComment())
                .date(visitAssignmentRequest.getDate())
                .build();

        visitAssignment.setVisitDefinition(visitDefinition);
        visitDefinition.getVisitAssignments().add(visitAssignment);
        visitDefinition = visitDefinitionRepository.save(visitDefinition);

        return ResponseEntity.status(HttpStatus.CREATED).body(visitDefinition.toDetailPayload());
    }
}
