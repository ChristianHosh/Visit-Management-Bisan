package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.model.visit.VisitDefinition;
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


    public VisitDefinitionController(VisitDefinitionService visitDefinitionService) {
        this.visitDefinitionService = visitDefinitionService;
    }

    @GetMapping("")
    public ResponseEntity<List<VisitDefinition>> getAllVisitDefinition() {
        List<VisitDefinition> visitDefinitionsList = visitDefinitionService.findAllVisitDefinition();

        return new ResponseEntity<>(visitDefinitionsList, HttpStatus.OK);
    }


    @GetMapping(value = "/search", params = "name")
    public ResponseEntity<List<VisitDefinition>> searchByName(@RequestParam("name") String name) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByName(name);
        return new ResponseEntity<>(visitDefinitionList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "description")
    public ResponseEntity<List<VisitDefinition>> searchByDescription(@RequestParam("description") String description) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByDescription(description);
        return new ResponseEntity<>(visitDefinitionList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "type")
    public ResponseEntity<List<VisitDefinition>> searchByType(@RequestParam("type") int type) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByType(type);
        return new ResponseEntity<>(visitDefinitionList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "frequency")
    public ResponseEntity<List<VisitDefinition>> searchByFrequency(@RequestParam("frequency") int frequency) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByFrequency(frequency);
        return new ResponseEntity<>(visitDefinitionList, HttpStatus.OK);
    }

    @GetMapping(value = "/recurring/true")
    public ResponseEntity<List<VisitDefinition>> getAllRecurringDefinitions() {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByAllowRecurring(true);
        return new ResponseEntity<>(visitDefinitionList, HttpStatus.OK);
    }

    @GetMapping(value = "/recurring/false")
    public ResponseEntity<List<VisitDefinition>> getAllNotRecurringDefinitions() {
        List<VisitDefinition> visitDefinitionList = visitDefinitionService.searchByAllowRecurring(false);
        return new ResponseEntity<>(visitDefinitionList, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<VisitDefinition> saveNewVisitDefinition(@RequestBody @Valid VisitDefinitionPostDTO visitDefinitionRequest) {
        VisitDefinition savedVisitDefinition = visitDefinitionService.saveNewVisit(visitDefinitionRequest);

        return new ResponseEntity<>(savedVisitDefinition, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<VisitDefinition> updateVisitDefinition(@PathVariable UUID id, @RequestBody @Valid VisitDefinitionPutDTO VisitDefinitionUpdate) {
        VisitDefinition visitDefinitionToUpdate = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (visitDefinitionToUpdate == null)
            throw new UserNotFoundException();

        VisitDefinition updatedVisitDefinition = visitDefinitionService.updateVisitDefinition(visitDefinitionToUpdate, VisitDefinitionUpdate);

        return new ResponseEntity<>(updatedVisitDefinition, HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<VisitDefinition> enableVisitDefinition(@PathVariable UUID id) {
        VisitDefinition VisitDefinitionToEnable = visitDefinitionService.findVisitDefinitionByUUID(id);

        if (VisitDefinitionToEnable == null)
            throw new UserNotFoundException();

        VisitDefinitionToEnable = visitDefinitionService.enableVisitDefinition(VisitDefinitionToEnable);

        return new ResponseEntity<>(VisitDefinitionToEnable, HttpStatus.OK);
    }


//    @PostMapping("{id}/assignments")
//    public ResponseEntity<VisitAssignment> saveAssignmentToDefinition(@PathVariable UUID id, @RequestBody VisitAssignment)


}