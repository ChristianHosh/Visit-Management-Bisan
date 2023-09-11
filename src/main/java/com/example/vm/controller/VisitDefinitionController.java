package com.example.vm.controller;

import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.request.VisitDefinitionRequest;
import com.example.vm.service.VisitDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/visit_definitions")
public class VisitDefinitionController {

    private final VisitDefinitionService visitDefinitionService;

    public VisitDefinitionController(VisitDefinitionService visitDefinitionService) {
        this.visitDefinitionService = visitDefinitionService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEnableVisitDefinition() {
        return visitDefinitionService.findAllEnabledVisitDefinitions();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllVisitDefinition() {
        return visitDefinitionService.findAllVisitDefinition();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitDefinitionById(@PathVariable Long id) {
        return visitDefinitionService.findVisitDefinitionByID(id);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<?> searchVisitDefinitions(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "enabled", required = false) Boolean enabled,
            @RequestParam(value = "recurring", required = false) Boolean recurring,
            @RequestParam(value = "type", required = false) Long typeId,
            @RequestParam(value = "city", required = false) Long cityId,
            @RequestParam(value = "location", required = false) Long locationId
    ) {
        return visitDefinitionService.searchVisitDefinitions(name, enabled, recurring, typeId, cityId, locationId);
    }


    @PostMapping("")
    public ResponseEntity<?> saveNewVisitDefinition(@RequestBody @Valid VisitDefinitionRequest visitDefinitionRequest) {
        return visitDefinitionService.saveNewVisitDefinition(visitDefinitionRequest);
    }

    @PostMapping("/{id}/visit_assignments")
    public ResponseEntity<?> saveNewVisitAssignmentToDefinition(@PathVariable Long id, @RequestBody @Valid VisitAssignmentRequest visitAssignmentRequest) {
        return visitDefinitionService.saveNewVisitAssignmentToDefinition(id, visitAssignmentRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisitDefinition(@PathVariable Long id, @RequestBody @Valid VisitDefinitionRequest visitDefinitionRequest) {
        return visitDefinitionService.updateVisitDefinition(id, visitDefinitionRequest);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableVisitDefinition(@PathVariable Long id) {
        return visitDefinitionService.enableVisitDefinition(id);
    }


}