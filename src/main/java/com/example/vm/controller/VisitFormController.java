package com.example.vm.controller;

import com.example.vm.dto.request.AssignmentCustomerRequest;
import com.example.vm.dto.request.form.FormCollectionRequest;
import com.example.vm.dto.request.form.FormRequest;
import com.example.vm.dto.request.form.FormSurveyRequest;
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
    public ResponseEntity<?> startForm(@PathVariable Long id, @RequestBody @Valid FormRequest formRequest) {
        return visitFormService.startForm(id, formRequest);
    }

    @PutMapping("/{id}/complete/collection")
    public ResponseEntity<?> completeForm(@PathVariable Long id, @RequestBody @Valid FormCollectionRequest formRequest) {
        return visitFormService.completeForm(id, formRequest);
    }

    @PutMapping("{id}/complete/survey")
    public ResponseEntity<?> completeForm(@PathVariable Long id, @RequestBody @Valid FormSurveyRequest formRequest) {
        return visitFormService.completeForm(id, formRequest);
    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> updateFormStatusCancel(@PathVariable Long id, @RequestBody @Valid FormSurveyRequest formSurveyRequest) {
        return visitFormService.cancelForm(id, formSurveyRequest);
    }

}
