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
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitForm;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.report.AssignmentReportListPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.example.vm.payload.list.VisitFormListPayload;
import com.example.vm.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VisitAssignmentService {
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final VisitFormRepository visitFormRepository;

    public VisitAssignmentService(VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, UserRepository userRepository, VisitFormRepository visitFormRepository) {
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.visitFormRepository = visitFormRepository;
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
    public ResponseEntity<List<VisitFormListPayload>> getFormsForAssignment(UUID id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));


        List<VisitForm> formList = visitFormRepository.findVisitFormByVisitAssignment(foundAssignment);

        return ResponseEntity.ok(VisitFormListPayload.toPayload(formList));

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

    public ResponseEntity<List<AssignmentReportListPayload>> reportAssignmentByDate(Date before,Date after) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(before);
        calendar.add(Calendar.DATE, -1);
        Date date1= calendar.getTime();
        Calendar calendarr = Calendar.getInstance();
        calendarr.setTime(after);
        calendarr.add((Calendar.DATE), 1);
        Date date2= calendarr.getTime();

        return ResponseEntity.ok(AssignmentReportListPayload.toPayload(
                visitAssignmentRepository.findVisitAssignmentByDateAfterAndDateBefore(date1,date2)));
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



        VisitForm newVisitForm = VisitForm.builder()
                .status(VisitStatus.NOT_STARTED)
                .customer(foundCustomer)
                .contacts(contactList)
                .visitAssignment(foundAssignment)
                .build();

        foundAssignment.getCustomers().add(foundCustomer);

        foundAssignment = visitAssignmentRepository.save(foundAssignment);
        visitFormRepository.save(newVisitForm);

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
        if (!currentAssignment.getVisitDefinition().isAllowRecurring())
            return;

        if (currentAssignment.getNextVisitAssignment() == null){

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
                    .nextVisitAssignment(null)
                    .enabled(1)
                    .build();

            currentAssignment.setNextVisitAssignment(nextAssignment);

            visitAssignmentRepository.save(nextAssignment);
            visitAssignmentRepository.save(currentAssignment);

            createNextAssignmentForm(currentCustomer, nextAssignment);

        }else {
            VisitAssignment nextAssignment = currentAssignment.getNextVisitAssignment();

            createNextAssignmentForm(currentCustomer, nextAssignment);

            visitAssignmentRepository.save(nextAssignment);
        }

    }

    private void createNextAssignmentForm(Customer currentCustomer, VisitAssignment currentAssignment) {
        if (!currentAssignment.getCustomers().contains(currentCustomer)){
            currentAssignment.getCustomers().add(currentCustomer);

            VisitType visitType = currentAssignment.getVisitDefinition().getType();

            List<Contact> contactList = contactRepository.findContactsByCustomerAndVisitTypesContaining(currentCustomer, visitType);

            VisitForm newVisitForm = VisitForm.builder()
                    .status(VisitStatus.NOT_STARTED)
                    .customer(currentCustomer)
                    .visitAssignment(currentAssignment)
                    .contacts(contactList)
                    .build();

            visitFormRepository.save(newVisitForm);
        }
        else
            throw new CustomerAlreadyAssignedException();
    }

}

