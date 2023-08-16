package com.example.vm.controller;

import com.example.vm.dto.AssignmentCustomerDTO;
import com.example.vm.dto.FormCompleteDTO;
import com.example.vm.service.VisitFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/visit_forms")
public class VisitFormController {

    private final VisitFormService visitFormService;

    @Autowired
    public VisitFormController(VisitFormService visitFormService) {
        this.visitFormService = visitFormService;
    }

    @PostMapping("")
    public ResponseEntity<?> createNewForm(@RequestBody @Valid AssignmentCustomerDTO assignmentCustomerDTO) {
        return visitFormService.createNewForm(assignmentCustomerDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFormById(@PathVariable UUID id) {
        return visitFormService.findVisitFormById(id);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> updateFormStatusComplete(@PathVariable UUID id, @RequestBody @Valid FormCompleteDTO geolocationDTO) {
        return visitFormService.updateFormStatusComplete(id, geolocationDTO);
    }


}
