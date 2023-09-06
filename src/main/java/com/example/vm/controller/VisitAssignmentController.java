package com.example.vm.controller;

import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.request.UnplannedVisitRequest;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.service.VisitAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/visit_assignments")
public class VisitAssignmentController {
    private final VisitAssignmentService visitAssignmentService;

    public VisitAssignmentController(VisitAssignmentService visitAssignmentService) {
        this.visitAssignmentService = visitAssignmentService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEnableVisitAssignment() {
        return visitAssignmentService.findAllEnabledVisitAssignments();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllVisitAssignment() {
        return visitAssignmentService.findAllVisitAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitAssignmentById(@PathVariable Long id) {
        return visitAssignmentService.findVisitAssignmentById(id);
    }

    @GetMapping("/{assignmentId}/customer/{customerId}/contacts")
    public ResponseEntity<?> getContactsByAssignmentType(@PathVariable Long assignmentId, @PathVariable Long customerId) {
        return visitAssignmentService.findCustomerContactsByAssignmentType(assignmentId, customerId);
    }

    @GetMapping("/{assignmentId}/forms")
    public ResponseEntity<?> getFormForAnAssignment(@PathVariable Long assignmentId) {
        return visitAssignmentService.getFormsByAssignment(assignmentId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id,
                                              @RequestBody @Valid VisitAssignmentRequest visitAssignmentUpdate) {
        return visitAssignmentService.updateVisitAssignment(id, visitAssignmentUpdate);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableVisitAssignment(@PathVariable Long id) {
        return visitAssignmentService.enableVisitAssignment(id);
    }

    @PutMapping("/{id}/customers")
    public ResponseEntity<?> assignCustomerToAssignment(@PathVariable(name = "id") Long assignmentId, @RequestBody @Valid Long customerId) {
        return visitAssignmentService.assignVisitToCustomer(assignmentId, customerId);
    }

    @DeleteMapping("/{id}/customers/{customerId}")
    public ResponseEntity<?> deleteCustomerFromAssignment(@PathVariable(name = "id") Long assignmentId, @PathVariable Long customerId) {
        return visitAssignmentService.deleteCustomerFromAssignment(assignmentId, customerId);
    }

    @PostMapping("/{assignmentId}/customers/{customerId}/contacts")
    public ResponseEntity<?> createContactAndAssignCustomerToAssignment(@PathVariable Long assignmentId, @PathVariable Long customerId, @RequestBody @Valid ContactRequest contactRequest){
        return visitAssignmentService.createContactAndAssignCustomerToAssignment(assignmentId, customerId, contactRequest);
    }
    @PostMapping("/{assignmentId}/new_visit")
    public ResponseEntity<?> UnplannedVisit(@PathVariable Long assignmentId, @RequestBody @Valid UnplannedVisitRequest unplannedVisit){
        return visitAssignmentService.createUnplannedVisit(assignmentId,unplannedVisit);
    }

    @PutMapping("/{id}/users")
    public ResponseEntity<?> assignVisitToUser(@PathVariable Long id, @RequestBody @Valid String username) {
        System.out.println(username);
        return visitAssignmentService.assignVisitToUser(id, username);
    }


}
