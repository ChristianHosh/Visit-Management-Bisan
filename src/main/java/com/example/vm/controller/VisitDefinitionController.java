package com.example.vm.controller;

import com.example.vm.dto.post.VisitAssignmentPostDTO;
import com.example.vm.dto.post.VisitDefinitionPostDTO;
import com.example.vm.dto.put.VisitDefinitionPutDTO;
import com.example.vm.payload.detail.VisitDefinitionDetailPayload;
import com.example.vm.service.VisitDefinitionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
//        return new ResponseEntity<>(toPayload(visitDefinitionList), HttpStatus.OK);
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
    public ResponseEntity<?> saveNewVisitDefinition(@RequestBody @Valid VisitDefinitionPostDTO visitDefinitionRequest) {
       return visitDefinitionService.saveNewVisit(visitDefinitionRequest);
    }


    @PostMapping("/{id}/assignments")
    public ResponseEntity<?> saveNewVisitAssignmentToDefinition(@PathVariable UUID id, @RequestBody @Valid VisitAssignmentPostDTO visitAssignmentRequest) {
        return visitDefinitionService.saveNewVisitAssignmentToDefinition(id, visitAssignmentRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisitDefinition(@PathVariable UUID id, @RequestBody @Valid VisitDefinitionPutDTO visitDefinitionRequest) {
        return visitDefinitionService.updateVisitDefinition(id, visitDefinitionRequest);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableVisitDefinition(@PathVariable UUID id) {
        return  visitDefinitionService.enableVisitDefinition(id);

    }


}