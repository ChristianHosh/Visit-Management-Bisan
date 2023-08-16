package com.example.vm.service;

import com.example.vm.controller.error.exception.CustomerAlreadyAssignedException;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.NoContactTypeException;
import com.example.vm.dto.UUIDDTO;
import com.example.vm.dto.UserDTO;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.example.vm.repository.ContactRepository;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.UserRepository;
import com.example.vm.repository.VisitAssignmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VisitAssignmentService {
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    public VisitAssignmentService(VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, UserRepository userRepository) {
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<List<VisitAssignmentListPayload>> findAllVisitAssignments() {
        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository.findAll();

        return ResponseEntity.ok(VisitAssignmentListPayload.toPayload(visitAssignmentList));
    }

    public ResponseEntity<VisitAssignmentDetailPayload> findVisitAssignmentByUUID(UUID id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<List<ContactListPayload>> findCustomerContactsByAssignmentType(UUID id, UUIDDTO customerUUID) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(customerUUID.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        if (!foundAssignment.getCustomers().contains(foundCustomer))
            throw new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_ASSIGNED);

        VisitType assignmentVisitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);

        return ResponseEntity.ok(ContactListPayload.toPayload(contactList));

    }

    public ResponseEntity<VisitAssignmentDetailPayload> updateVisitAssignment(UUID id, VisitAssignmentPutDTO assignmentRequest) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        foundAssignment.setComment(assignmentRequest.getComment());
        foundAssignment.setDate(assignmentRequest.getDate());

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<VisitAssignmentDetailPayload> enableVisitAssignment(UUID id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        foundAssignment.setEnabled(foundAssignment.getEnabled() == 0 ? 1 : 0);

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<VisitAssignmentDetailPayload> assignVisitToCustomer(UUID id, UUIDDTO customerUUID) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(customerUUID.getUuid())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        VisitType assignmentVisitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);

        if (contactList.isEmpty())
            throw new NoContactTypeException();

        if (foundAssignment.getCustomers().contains(foundCustomer))
            throw new CustomerAlreadyAssignedException();


        foundAssignment.getCustomers().add(foundCustomer);

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<VisitAssignmentDetailPayload> assignVisitToUser(UUID id, UserDTO userDTO) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        User foundUser = userRepository.findById(userDTO.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        foundAssignment.setUser(foundUser);

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public void createNextVisitAssignment(VisitAssignment currentAssignment, Customer currentCustomer){
        System.out.println("CUSTOMER :" + currentCustomer.getName());

        if (!currentAssignment.getVisitDefinition().isAllowRecurring())
            return;

        if (currentAssignment.getNextVisitAssignment() == null){
            System.out.println("CURRENT ASSIGNMENT DOES NOT HAVE A NEXT");

            int frequency = currentAssignment.getVisitDefinition().getFrequency();

            Date currentDate = currentAssignment.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);
            calendar.add(Calendar.DATE, frequency);

            Date nextAssignmentDate = calendar.getTime();

            VisitAssignment nextAssignment = VisitAssignment.builder()
                    .visitDefinition(currentAssignment.getVisitDefinition())
                    .comment(currentAssignment.getComment())
                    .user(currentAssignment.getUser())
                    .customers(new ArrayList<>())
                    .date(new java.sql.Date(nextAssignmentDate.getTime()))
                    .enabled(1)
                    .build();

            if (!nextAssignment.getCustomers().contains(currentCustomer))
                nextAssignment.getCustomers().add(currentCustomer);
            else
                throw new CustomerAlreadyAssignedException();

            currentAssignment.setNextVisitAssignment(nextAssignment);

            visitAssignmentRepository.save(currentAssignment);

        }else {
            System.out.println("CURRENT ASSIGNMENT DOES HAVE A NEXT");

            VisitAssignment nextAssignment = currentAssignment.getNextVisitAssignment();

            if (!nextAssignment.getCustomers().contains(currentCustomer))
                nextAssignment.getCustomers().add(currentCustomer);
            else
                throw new CustomerAlreadyAssignedException();

            visitAssignmentRepository.save(nextAssignment);
        }

    }

}

