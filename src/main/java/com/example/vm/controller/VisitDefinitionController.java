package com.example.vm.controller;

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
import com.example.vm.service.VisitAssignmentService;
import com.example.vm.service.VisitDefinitionService;
import com.example.vm.service.VisitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/visit_definitions")
public class VisitDefinitionController {
    private final VisitDefinitionService visitDefinitionService;
    private final VisitAssignmentService visitAssignmentService;
    private final VisitTypeService visitTypeService;


    public VisitDefinitionController(VisitDefinitionService visitDefinitionService, VisitAssignmentService visitAssignmentService, VisitTypeService visitTypeService) {
        this.visitDefinitionService = visitDefinitionService;
        this.visitAssignmentService = visitAssignmentService;
        this.visitTypeService = visitTypeService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllVisitDefinition() {
       return visitDefinitionService.findAllVisitDefinition();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitDefinitionDetailPayload> getVisitDefinitionById(@PathVariable UUID id) {
      return visitDefinitionService.findVisitDefinitionByUUID(id);

    }


    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<?> searchByName(@RequestParam("name") String name) {
        return  visitDefinitionService.searchByName(name);
    }



    //TODO FIX SEARCH BY TYPE
//    @GetMapping(value = "/search", params = "type")
//    public ResponseEntity<List<VisitDefinitionListPayload>> searchByType(@RequestParam("type") int type) {
//        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByType(type);
//
//        return new ResponseEntity<>(toDefinitionPayloadList(visitDefinitionList), HttpStatus.OK);
//    }

    @GetMapping(value = "/search", params = "frequency")
    public ResponseEntity<?> searchByFrequency(@RequestParam("frequency") int frequency) {
        return visitDefinitionService.searchByFrequency(frequency);
    }

    @GetMapping(value = "/recurring/true")
    public ResponseEntity<?> getAllRecurringDefinitions() {
      return visitDefinitionService.searchByAllowRecurring(true);
    }

    @GetMapping(value = "/recurring/false")
    public ResponseEntity<?> getAllNotRecurringDefinitions() {
       return visitDefinitionService.searchByAllowRecurring(false);

    }


    @PostMapping("")
    public ResponseEntity<VisitDefinitionDetailPayload> saveNewVisitDefinition(@RequestBody @Valid VisitDefinitionPostDTO visitDefinitionRequest) {
       return visitTypeService.findById(visitDefinitionRequest.getTypeUUID());

        if (visitType == null){
            throw new UserNotFoundException(UserNotFoundException.TYPE_NOT_FOUND);
        }

        VisitDefinition savedVisitDefinition = visitDefinitionService.saveNewVisit(visitDefinitionRequest, visitType);
        savedVisitDefinition.setVisitAssignments(new ArrayList<>());

        return new ResponseEntity<>(savedVisitDefinition.toDetailPayload(), HttpStatus.CREATED);
    }


    @PostMapping("/{id}/assignments")
    public ResponseEntity<VisitAssignmentDetailPayload> saveNewVisitAssignmentToDefinition(@PathVariable UUID id, @RequestBody @Valid VisitAssignmentPostDTO visitAssignmentRequest) {
        VisitDefinition visitDefinition = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinition == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitAssignment savedVisitAssignment = visitAssignmentService.saveNewVisitAssignment(visitDefinition, visitAssignmentRequest);
        savedVisitAssignment.setCustomers(new ArrayList<>());

        return new ResponseEntity<>(savedVisitAssignment.toDetailPayload(), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VisitDefinitionDetailPayload> updateVisitDefinition(@PathVariable UUID id, @RequestBody @Valid VisitDefinitionPutDTO VisitDefinitionUpdate) {
        VisitDefinition visitDefinitionToUpdate = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinitionToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.DEFINITION_NOT_FOUND);

        VisitType visitType = visitTypeService.findById(VisitDefinitionUpdate.getTypeUUID());

        if (visitType == null){
            throw new UserNotFoundException(UserNotFoundException.TYPE_NOT_FOUND);
        }

        VisitDefinition updatedVisitDefinition = visitDefinitionService.updateVisitDefinition(visitDefinitionToUpdate, VisitDefinitionUpdate, visitType);

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


}