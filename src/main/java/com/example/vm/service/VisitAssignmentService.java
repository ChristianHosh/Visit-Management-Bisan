package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.CustomerAlreadyAssignedException;
import com.example.vm.controller.error.exception.EntityNotEnabled;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.NoContactTypeException;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitForm;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.VisitAssignmentListPayload;
import com.example.vm.payload.list.VisitFormListPayload;
import com.example.vm.payload.report.AssignmentReportListPayload;
import com.example.vm.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    public ResponseEntity<List<VisitAssignmentListPayload>> findAllEnableVisitAssignments() {
        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository.findVisitAssignmentsByEnabled(true);

        return ResponseEntity.ok(VisitAssignmentListPayload.toPayload(visitAssignmentList));
    }

    public ResponseEntity<VisitAssignmentDetailPayload> findVisitAssignmentById(Long id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<List<ContactListPayload>> findCustomerContactsByAssignmentType(Long assignmentId, Long customerId) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        if (!foundAssignment.getCustomers().contains(foundCustomer))
            throw new CustomerAlreadyAssignedException();

        VisitType assignmentVisitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);

        return ResponseEntity.ok(ContactListPayload.toPayload(contactList));

    }

    public ResponseEntity<List<VisitFormListPayload>> getFormsByAssignment(Long id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        List<VisitForm> formList = visitFormRepository.findVisitFormByVisitAssignment(foundAssignment);

        return ResponseEntity.ok(VisitFormListPayload.toPayload(formList));

    }

    public ResponseEntity<VisitAssignmentDetailPayload> updateVisitAssignment(Long id, VisitAssignmentRequest assignmentRequest) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));


        // FIND DATE MAKE SURE IT HAS NOT REACHED THAT DATE
        Date currentAssignmentDate = foundAssignment.getDate();

        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, -1);
        todayDate.setTime(calendar.getTime().getTime());

        if (assignmentRequest.getDate().before(todayDate)){
            // THROW INVALID ARGUMENT EXCEPTION
            System.out.println("DATE MUST BE IN THE PRESENT OR FUTURE");
            return null;
        }

        if (currentAssignmentDate.before(todayDate)){
            // THROW ASSIGNMENT TOO OLD EXCEPTION
            System.out.println("ASSIGNMENT TOO OLD !!!!!!");
            return null;
        }

        foundAssignment.setComment(assignmentRequest.getComment());
        foundAssignment.setDate(assignmentRequest.getDate());

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<VisitAssignmentDetailPayload> enableVisitAssignment(Long id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        foundAssignment.setEnabled(!foundAssignment.getEnabled());

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public ResponseEntity<List<AssignmentReportListPayload>> reportAssignmentByDate(Date before, Date after) {
        return ResponseEntity.ok(AssignmentReportListPayload.toPayload(
                visitAssignmentRepository.findVisitAssignmentByDateBetween(before, after)));
    }


    public ResponseEntity<VisitAssignmentDetailPayload> assignVisitToCustomer(Long assignmentId, Long customerId) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        VisitType assignmentVisitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);

        if (!foundAssignment.getEnabled())
            throw new EntityNotEnabled(ErrorMessage.ASSIGNMENT_NOT_ENABLED);

        if (!foundCustomer.getEnabled())
            throw new EntityNotEnabled(ErrorMessage.CUSTOMER_NOT_ENABLED);

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

    public ResponseEntity<VisitAssignmentDetailPayload> assignVisitToUser(Long id, String username) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        User foundUser = userRepository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        if (!foundAssignment.getEnabled())
            throw new EntityNotEnabled(ErrorMessage.ASSIGNMENT_NOT_ENABLED);

        if (!foundUser.getEnabled())
            throw new EntityNotEnabled(ErrorMessage.USER_NOT_ENABLED);

        foundAssignment.setUser(foundUser);

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(foundAssignment.toDetailPayload());
    }

    public void createNextVisitAssignment(VisitAssignment currentAssignment, Customer currentCustomer) {
        if (!currentAssignment.getVisitDefinition().isAllowRecurring())
            return;

        if (currentAssignment.getNextVisitAssignment() == null) {

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
                    .build();

            currentAssignment.setNextVisitAssignment(nextAssignment);

            visitAssignmentRepository.save(nextAssignment);
            visitAssignmentRepository.save(currentAssignment);

            createNextAssignmentForm(currentCustomer, nextAssignment);

        } else {
            VisitAssignment nextAssignment = currentAssignment.getNextVisitAssignment();

            createNextAssignmentForm(currentCustomer, nextAssignment);

            visitAssignmentRepository.save(nextAssignment);
        }

    }

    private void createNextAssignmentForm(Customer currentCustomer, VisitAssignment currentAssignment) {
        if (!currentAssignment.getCustomers().contains(currentCustomer)) {
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
        } else
            throw new CustomerAlreadyAssignedException();
    }

}

