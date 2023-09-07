package com.example.vm.controller;

import com.example.vm.dto.request.AssignmentCustomerRequest;
import com.example.vm.dto.request.FormUpdateRequest;
import com.example.vm.service.VisitFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> createNewForm(@RequestBody @Valid AssignmentCustomerRequest assignmentCustomerRequest) {
        return visitFormService.createNewForm(assignmentCustomerRequest);
    }

    @GetMapping("{id}/contacts")
    public ResponseEntity<?> getFormContactsByFormId(@PathVariable Long id) {
        return visitFormService.findFormContactsByFormId(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFormById(@PathVariable Long id) {
        return visitFormService.findVisitFormById(id);
    }

    @PutMapping("/{id}/start")
    public ResponseEntity<?> updateFormStatusUndergoing(@PathVariable Long id, @RequestBody @Valid FormUpdateRequest formUpdateRequest) {
        return visitFormService.startForm(id, formUpdateRequest);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> updateFormStatusComplete(@PathVariable Long id, @RequestBody @Valid FormUpdateRequest formUpdateRequest) {
        return visitFormService.completeForm(id, formUpdateRequest);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> updateFormStatusCancel(@PathVariable Long id, @RequestBody @Valid FormUpdateRequest formUpdateRequest) {
        return visitFormService.cancelForm(id, formUpdateRequest);
    }


}
