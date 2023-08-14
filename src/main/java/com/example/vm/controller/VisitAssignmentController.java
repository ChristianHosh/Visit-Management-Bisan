package com.example.vm.controller;

import com.example.vm.dto.UUIDDTO;
import com.example.vm.dto.UserDTO;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.service.VisitAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/visit_assignments")
public class VisitAssignmentController {
    private final VisitAssignmentService visitAssignmentService;

    public VisitAssignmentController(VisitAssignmentService visitAssignmentService) {
        this.visitAssignmentService = visitAssignmentService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllVisitAssignment() {
        return visitAssignmentService.findAllVisitAssignments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVisitAssignmentById(@PathVariable UUID id) {
        return visitAssignmentService.findVisitAssignmentByUUID(id);
    }

    @GetMapping("/{assignmentId}/customer_contacts")
    public ResponseEntity<?> getContactsByAssignmentType(@PathVariable UUID assignmentId, @RequestBody @Valid UUIDDTO customerUUIDDTO) {
        return visitAssignmentService.findCustomerContactsByAssignmentType(assignmentId, customerUUIDDTO);
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(@PathVariable UUID id,
                                              @RequestBody @Valid VisitAssignmentPutDTO visitAssignmentUpdate) {
        return visitAssignmentService.updateVisitAssignment(id, visitAssignmentUpdate);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableCustomer(@PathVariable UUID id) {
        return visitAssignmentService.enableVisitAssignment(id);
    }

    @PostMapping("/{id}/customers")
    public ResponseEntity<?> assignVisitToCustomer(@PathVariable UUID id, @RequestBody @Valid UUIDDTO customerUUID) {
        return visitAssignmentService.assignVisitToCustomer(id, customerUUID);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<?> assignVisitToUser(@PathVariable UUID id, @RequestBody @Valid UserDTO userDTO) {
        return visitAssignmentService.assignVisitToUser(id, userDTO);
    }


}
