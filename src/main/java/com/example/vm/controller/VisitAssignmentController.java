package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.UUIDDTO;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.payload.VisitAssignmentDetailedPayload;
import com.example.vm.payload.VisitAssignmentPayload;
import com.example.vm.service.CustomerService;
import com.example.vm.service.VisitAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/assignments")
public class VisitAssignmentController {
    private final VisitAssignmentService visitAssignmentService;
    private final CustomerService customerService;

    public VisitAssignmentController(VisitAssignmentService visitAssignmentService, CustomerService customerService) {
        this.visitAssignmentService = visitAssignmentService;
        this.customerService = customerService;
    }

    @GetMapping("")
    public ResponseEntity<List<VisitAssignmentPayload>> getAllVisitAssignment() {
        List<VisitAssignment> visitAssignmentList = visitAssignmentService.findAllVisitAssignments();

        return new ResponseEntity<>(toPayloadList(visitAssignmentList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitAssignmentDetailedPayload> getVisitAssignmentById(@PathVariable UUID id) {
        VisitAssignment visitAssignment = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignment == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        return new ResponseEntity<>(visitAssignment.toDetailedPayload(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitAssignmentDetailedPayload> updateContact(@PathVariable UUID id,
                                                         @RequestBody @Valid VisitAssignmentPutDTO visitAssignmentUpdate) {

        VisitAssignment visitAssignmentToUpdate = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignmentToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        VisitAssignment updatedVisitAssignment = visitAssignmentService.updateVisitAssignment(visitAssignmentToUpdate, visitAssignmentUpdate);

        return new ResponseEntity<>(updatedVisitAssignment.toDetailedPayload(), HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<VisitAssignmentDetailedPayload> enableCustomer(@PathVariable UUID id) {
        VisitAssignment visitAssignmentToEnable = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignmentToEnable == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        visitAssignmentToEnable = visitAssignmentService.enableVisitAssignment(visitAssignmentToEnable);

        return new ResponseEntity<>(visitAssignmentToEnable.toDetailedPayload(), HttpStatus.OK);
    }

    @PostMapping("/{id}/customers")
    public ResponseEntity<VisitAssignmentDetailedPayload> assignVisitToCustomer(@PathVariable UUID id, @RequestBody @Valid UUIDDTO uuidDTO) {
        VisitAssignment visitAssignment = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignment == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        Customer customer = customerService.findCustomerByUUID(uuidDTO.getUuid());

        if (customer == null)
            throw new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND);

        visitAssignment = visitAssignmentService.assignVisitToCustomer(visitAssignment, customer);

        return new ResponseEntity<>(visitAssignment.toDetailedPayload(), HttpStatus.OK);
    }

    private static List<VisitAssignmentPayload> toPayloadList(List<VisitAssignment> assignmentList){
        return assignmentList.stream().map(VisitAssignment::toPayload).toList();
    }
}
