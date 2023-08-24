package com.example.vm.controller;

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

        return visitAssignmentService.findAllEnableVisitAssignments();
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllVisitAssignment() {

        return visitAssignmentService.findAllVisitAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitAssignmentById(@PathVariable Long id) {
        return visitAssignmentService.findVisitAssignmentById(id);
    }

    @GetMapping("/{assignmentId}/customer_contacts")
    public ResponseEntity<?> getContactsByAssignmentType(@PathVariable Long assignmentId, @RequestBody @Valid Long customerId) {
        return visitAssignmentService.findCustomerContactsByAssignmentType(assignmentId, customerId);
    }
    @GetMapping("/{assignmentId}/forms")
    public ResponseEntity<?> getFormForAnAssignment(@PathVariable Long assignmentId){
        return visitAssignmentService.getFormsByAssignment(assignmentId);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable Long id,
                                              @RequestBody @Valid VisitAssignmentRequest visitAssignmentUpdate) {
        return visitAssignmentService.updateVisitAssignment(id, visitAssignmentUpdate);
    }
    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableCustomer(@PathVariable Long id) {
        return visitAssignmentService.enableVisitAssignment(id);
    }
    @PostMapping("/{id}/customers")
    public ResponseEntity<?> assignVisitToCustomer(@PathVariable(name = "id") Long assignmentId, @RequestBody @Valid Long customerId) {
        return visitAssignmentService.assignVisitToCustomer(assignmentId, customerId);
    }
    @PostMapping("/{id}/users")
    public ResponseEntity<?> assignVisitToUser(@PathVariable Long id, @RequestBody @Valid String username) {
        System.out.println(username);
        return visitAssignmentService.assignVisitToUser(id, username);
    }


}
