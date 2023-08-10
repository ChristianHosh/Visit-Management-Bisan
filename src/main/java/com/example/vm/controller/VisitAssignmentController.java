package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.UUIDDTO;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.example.vm.service.ContactService;
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
@RequestMapping("/visit_assignments")
public class VisitAssignmentController {
    private final VisitAssignmentService visitAssignmentService;
    private final CustomerService customerService;
    private final ContactService contactService;

    public VisitAssignmentController(VisitAssignmentService visitAssignmentService, CustomerService customerService, ContactService contactService) {
        this.visitAssignmentService = visitAssignmentService;
        this.customerService = customerService;
        this.contactService = contactService;
    }

    @GetMapping("")
    public ResponseEntity<List<VisitAssignmentListPayload>> getAllVisitAssignment() {
        List<VisitAssignment> visitAssignmentList = visitAssignmentService.findAllVisitAssignments();

        return new ResponseEntity<>(toPayloadList(visitAssignmentList), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitAssignmentDetailPayload> getVisitAssignmentById(@PathVariable UUID id) {
        VisitAssignment visitAssignment = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignment == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        return new ResponseEntity<>(visitAssignment.toDetailPayload(), HttpStatus.OK);
    }

    @GetMapping("/{assignmentId}/customer_contacts")
    public ResponseEntity<List<Contact>> getContactsByAssignmentType(@PathVariable UUID assignmentId, @RequestBody @Valid UUIDDTO customerUUIDDTO){
        VisitAssignment currentAssignment = visitAssignmentService.findVisitAssignmentByUUID(assignmentId);

        if (currentAssignment == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        Customer currentCustomer = customerService.findCustomerByUUID(customerUUIDDTO.getUuid());

        if (currentCustomer == null)
            throw new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND);

        if (!currentAssignment.getCustomers().contains(currentCustomer))
            throw new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_ASSIGNED);

        VisitType currentVisitType = currentAssignment.getVisitDefinition().getType();

        List<Contact> contactList = contactService.findContactsByCustomerAndVisitTypes(currentCustomer, currentVisitType);

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitAssignmentDetailPayload> updateContact(@PathVariable UUID id,
                                                                      @RequestBody @Valid VisitAssignmentPutDTO visitAssignmentUpdate) {

        VisitAssignment visitAssignmentToUpdate = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignmentToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        VisitAssignment updatedVisitAssignment = visitAssignmentService.updateVisitAssignment(visitAssignmentToUpdate, visitAssignmentUpdate);

        return new ResponseEntity<>(updatedVisitAssignment.toDetailPayload(), HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<VisitAssignmentDetailPayload> enableCustomer(@PathVariable UUID id) {
        VisitAssignment visitAssignmentToEnable = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignmentToEnable == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        visitAssignmentToEnable = visitAssignmentService.enableVisitAssignment(visitAssignmentToEnable);

        return new ResponseEntity<>(visitAssignmentToEnable.toDetailPayload(), HttpStatus.OK);
    }

    @PostMapping("/{id}/customers")
    public ResponseEntity<VisitAssignmentDetailPayload> assignVisitToCustomer(@PathVariable UUID id, @RequestBody @Valid UUIDDTO uuidDTO) {
        VisitAssignment visitAssignment = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignment == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        Customer customer = customerService.findCustomerByUUID(uuidDTO.getUuid());

        if (customer == null)
            throw new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND);

        visitAssignment = visitAssignmentService.assignVisitToCustomer(visitAssignment, customer);

        return new ResponseEntity<>(visitAssignment.toDetailPayload(), HttpStatus.OK);
    }

    private static List<VisitAssignmentListPayload> toPayloadList(List<VisitAssignment> assignmentList){
        return assignmentList.stream().map(VisitAssignment::toListPayload).toList();
    }
}
