package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.detail.VisitDefinitionDetailPayload;
import com.example.vm.payload.list.VisitDefinitionListPayload;
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
    public ResponseEntity<List<VisitDefinitionListPayload>> getAllVisitDefinition() {
        List<VisitDefinition> visitDefinitionsList = visitDefinitionService.findAllVisitDefinition();

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionsList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDefinitionDetailPayload> getVisitDefinitionById(@PathVariable UUID id) {
        VisitDefinition user = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (user == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        return new ResponseEntity<>(user.toDetailPayload(), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<List<VisitDefinitionListPayload>> searchByName(@RequestParam("name") String name) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByName(name);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "description")
    public ResponseEntity<List<VisitDefinitionListPayload>> searchByDescription(@RequestParam("description") String description) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByDescription(description);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "type")
    public ResponseEntity<List<VisitDefinitionListPayload>> searchByType(@RequestParam("type") int type) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByType(type);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "frequency")
    public ResponseEntity<List<VisitDefinitionListPayload>> searchByFrequency(@RequestParam("frequency") int frequency) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByFrequency(frequency);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/recurring/true")
    public ResponseEntity<List<VisitDefinitionListPayload>> getAllRecurringDefinitions() {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByAllowRecurring(true);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }

    @GetMapping(value = "/recurring/false")
    public ResponseEntity<List<VisitDefinitionListPayload>> getAllNotRecurringDefinitions() {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByAllowRecurring(false);

        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisitDefinition(@RequestBody @Valid VisitDefinitionPostDTO visitDefinitionRequest) {
        VisitDefinition savedVisitDefinition = visitDefinitionService.saveNewVisit(visitDefinitionRequest);

        return new ResponseEntity<>(savedVisitDefinition.toDetailPayload(), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/assignments")
    public ResponseEntity<VisitAssignmentDetailPayload> saveNewVisitAssignmentToDefinition(@PathVariable UUID id, @RequestBody @Valid VisitAssignmentPostDTO visitAssignmentRequest) {
        VisitDefinition visitDefinition = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinition == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitAssignment savedVisitAssignment = visitAssignmentService.saveNewVisitAssignment(visitDefinition, visitAssignmentRequest);

        return new ResponseEntity<>(savedVisitAssignment.toDetailPayload(), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VisitDefinitionDetailPayload> updateVisitDefinition(@PathVariable UUID id, @RequestBody @Valid VisitDefinitionPutDTO VisitDefinitionUpdate) {
        VisitDefinition visitDefinitionToUpdate = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinitionToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitDefinition updatedVisitDefinition = visitDefinitionService.updateVisitDefinition(visitDefinitionToUpdate, VisitDefinitionUpdate);

        return new ResponseEntity<>(updatedVisitDefinition.toDetailPayload(), HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<VisitDefinitionDetailPayload> enableVisitDefinition(@PathVariable UUID id) {
        VisitDefinition VisitDefinitionToEnable = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (VisitDefinitionToEnable == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitDefinitionToEnable = visitDefinitionService.enableVisitDefinition(VisitDefinitionToEnable);

        return new ResponseEntity<>(VisitDefinitionToEnable.toDetailPayload(), HttpStatus.OK);
    }

    private static List<VisitDefinitionListPayload> toDefinitionPayloadList(List<VisitDefinition> visitDefinitionList) {
        return visitDefinitionList.stream().map(VisitDefinition::toListPayload).toList();
    }
}