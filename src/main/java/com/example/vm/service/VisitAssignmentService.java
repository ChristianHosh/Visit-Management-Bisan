package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.*;
import com.example.vm.dto.mapper.ContactMapper;
import com.example.vm.dto.mapper.VisitAssignmentMapper;
import com.example.vm.dto.mapper.VisitFormMapper;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.response.ContactResponse;
import com.example.vm.dto.response.VisitAssignmentResponse;
import com.example.vm.dto.response.VisitFormResponse;
import com.example.vm.model.City;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitForm;
import com.example.vm.model.VisitType;
import com.example.vm.payload.report.AssignmentReportListPayload;
import com.example.vm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public VisitAssignmentService(VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, UserRepository userRepository, VisitFormRepository visitFormRepository) {
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.visitFormRepository = visitFormRepository;
    }

    public ResponseEntity<List<VisitAssignmentResponse>> findAllVisitAssignments() {
        List<VisitAssignment> queryResult = visitAssignmentRepository.findAll();

        return ResponseEntity.ok(VisitAssignmentMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<VisitAssignmentResponse>> findAllEnabledVisitAssignments() {
        List<VisitAssignment> queryResult = visitAssignmentRepository.findVisitAssignmentsByEnabledTrue();

        return ResponseEntity.ok(VisitAssignmentMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<VisitAssignmentResponse> findVisitAssignmentById(Long id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        return ResponseEntity.ok(VisitAssignmentMapper.toDetailedResponse(foundAssignment));
    }

    public ResponseEntity<List<ContactResponse>> findCustomerContactsByAssignmentType(Long assignmentId, Long customerId) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        if (!foundAssignment.getCustomers().contains(foundCustomer))
            throw new CustomerAlreadyAssignedException();

        VisitType assignmentVisitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);

        return ResponseEntity.ok(ContactMapper.listToResponseList(contactList));

    }

    public ResponseEntity<List<VisitFormResponse>> getFormsByAssignment(Long id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        List<VisitForm> formList = visitFormRepository.findVisitFormByVisitAssignment(foundAssignment);

        return ResponseEntity.ok(VisitFormMapper.listToRespnoseList(formList));

    }

    public ResponseEntity<VisitAssignmentResponse> updateVisitAssignment(Long id, VisitAssignmentRequest assignmentRequest) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));


        // FIND DATE MAKE SURE IT HAS NOT REACHED THAT DATE
        Date currentAssignmentDate = foundAssignment.getDate();

        Date todayDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(todayDate);
        calendar.add(Calendar.DATE, -1);
        todayDate.setTime(calendar.getTime().getTime());

        // VALIDATES THE DATE TO MAKE SURE IT IN THE PRESENT OR FUTURE
        if (assignmentRequest.getDate().before(todayDate))
            throw new InvalidDateException(ErrorMessage.DATE_IN_PAST);

        // VALIDATES THE CURRENT DATE TO MAKE SURE IT HAS NOT BEEN PASSED YET
        if (currentAssignmentDate.before(todayDate))
            throw new InvalidDateException(ErrorMessage.DATE_TOO_OLD);

        foundAssignment.setComment(assignmentRequest.getComment());
        foundAssignment.setDate(assignmentRequest.getDate());

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(VisitAssignmentMapper.toDetailedResponse(foundAssignment));
    }

    public ResponseEntity<VisitAssignmentResponse> enableVisitAssignment(Long id) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        foundAssignment.setEnabled(!foundAssignment.getEnabled());

        foundAssignment = visitAssignmentRepository.save(foundAssignment);

        return ResponseEntity.ok(VisitAssignmentMapper.toDetailedResponse(foundAssignment));
    }

    //TODO FIX REPORTS
    public ResponseEntity<List<AssignmentReportListPayload>> reportAssignmentByDate(Date before, Date after) {
        return ResponseEntity.ok(AssignmentReportListPayload.toPayload(
                visitAssignmentRepository.findVisitAssignmentByDateBetween(before, after)));
    }


    public ResponseEntity<VisitAssignmentResponse> assignVisitToCustomer(Long assignmentId, Long customerId) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findByIdAndEnabledTrue(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findByIdAndEnabledTrue(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        VisitType assignmentVisitType = foundAssignment.getVisitDefinition().getType();

        City assignmentCity = foundAssignment.getVisitDefinition().getCity();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);


        if (!foundCustomer.getAddress().getCity().equals(assignmentCity))
            throw new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_IN_CITY);

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

        return ResponseEntity.ok(VisitAssignmentMapper.toDetailedResponse(foundAssignment));
    }

    public ResponseEntity<VisitAssignmentResponse> assignVisitToUser(Long id, String username) {
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

        return ResponseEntity.ok(VisitAssignmentMapper.toDetailedResponse(foundAssignment));
    }

    public void createNextVisitAssignment(VisitAssignment currentAssignment, Customer currentCustomer) {
        if (!currentAssignment.getVisitDefinition().getAllowRecurring())
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

