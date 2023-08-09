package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.payload.VisitAssignmentDetailedPayload;
import com.example.vm.payload.VisitDefinitionPayload;
import com.example.vm.service.VisitAssignmentService;
import com.example.vm.service.VisitDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/visit_definitions")
public class VisitDefinitionController {
    private final VisitDefinitionService visitDefinitionService;
    private final VisitAssignmentService visitAssignmentService;


    public VisitDefinitionController(VisitDefinitionService visitDefinitionService, VisitAssignmentService visitAssignmentService) {
        this.visitDefinitionService = visitDefinitionService;
        this.visitAssignmentService = visitAssignmentService;
    }

    @GetMapping("")
    public ResponseEntity<List<VisitDefinitionPayload>> getAllVisitDefinition() {
        List<VisitDefinition> visitDefinitionsList = visitDefinitionService.findAllVisitDefinition();

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionsList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDefinition> getVisitDefinitionById(@PathVariable UUID id) {
        VisitDefinition user = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (user == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<List<VisitDefinitionPayload>> searchByName(@RequestParam("name") String name) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByName(name);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "description")
    public ResponseEntity<List<VisitDefinitionPayload>> searchByDescription(@RequestParam("description") String description) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByDescription(description);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "type")
    public ResponseEntity<List<VisitDefinitionPayload>> searchByType(@RequestParam("type") int type) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByType(type);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "frequency")
    public ResponseEntity<List<VisitDefinitionPayload>> searchByFrequency(@RequestParam("frequency") int frequency) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByFrequency(frequency);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/recurring/true")
    public ResponseEntity<List<VisitDefinitionPayload>> getAllRecurringDefinitions() {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByAllowRecurring(true);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/recurring/false")
    public ResponseEntity<List<VisitDefinitionPayload>> getAllNotRecurringDefinitions() {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByAllowRecurring(false);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<VisitDefinition> saveNewVisitDefinition(@RequestBody @Valid VisitDefinitionPostDTO visitDefinitionRequest) {
        VisitDefinition savedVisitDefinition = visitDefinitionService.saveNewVisit(visitDefinitionRequest);

        return new ResponseEntity<>(savedVisitDefinition, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/assignments")
    public ResponseEntity<VisitAssignmentDetailedPayload> saveNewVisitAssignmentToDefinition(@PathVariable UUID id, @RequestBody @Valid VisitAssignmentPostDTO visitAssignmentRequest) {
        VisitDefinition visitDefinition = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinition == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitAssignment savedVisitAssignment = visitAssignmentService.saveNewVisitAssignment(visitDefinition, visitAssignmentRequest);

        return new ResponseEntity<>(savedVisitAssignment.toDetailedPayload(), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VisitDefinition> updateVisitDefinition(@PathVariable UUID id, @RequestBody @Valid VisitDefinitionPutDTO VisitDefinitionUpdate) {
        VisitDefinition visitDefinitionToUpdate = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinitionToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitDefinition updatedVisitDefinition = visitDefinitionService.updateVisitDefinition(visitDefinitionToUpdate, VisitDefinitionUpdate);

        return new ResponseEntity<>(updatedVisitDefinition, HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<VisitDefinition> enableVisitDefinition(@PathVariable UUID id) {
        VisitDefinition VisitDefinitionToEnable = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (VisitDefinitionToEnable == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitDefinitionToEnable = visitDefinitionService.enableVisitDefinition(VisitDefinitionToEnable);

        return new ResponseEntity<>(VisitDefinitionToEnable, HttpStatus.OK);
    }

    private static List<VisitDefinitionPayload> toDefinitionPayloadList(List<VisitDefinition> visitDefinitionList) {
        return visitDefinitionList.stream().map(VisitDefinition::toPayload).toList();
    }
}