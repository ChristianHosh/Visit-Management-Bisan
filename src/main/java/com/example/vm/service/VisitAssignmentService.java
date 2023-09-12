package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.*;
import com.example.vm.dto.mapper.ContactMapper;
import com.example.vm.dto.mapper.VisitAssignmentMapper;
import com.example.vm.dto.mapper.VisitFormMapper;
import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.request.UnplannedVisitRequest;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.response.ContactResponse;
import com.example.vm.dto.response.VisitAssignmentResponse;
import com.example.vm.dto.response.VisitFormResponse;
import com.example.vm.model.*;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.report.AssignmentReportListPayload;
import com.example.vm.repository.*;
import com.example.vm.service.util.CalenderDate;
import com.example.vm.service.util.PhoneNumberFormatter;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final VisitTypeService visitTypeService;
    @Autowired
    public VisitAssignmentService(VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, UserRepository userRepository, VisitFormRepository visitFormRepository, VisitTypeService visitTypeService) {
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.visitFormRepository = visitFormRepository;
        this.visitTypeService = visitTypeService;
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

        // VALIDATES THE DATE TO MAKE SURE IT IN THE PRESENT OR FUTURE
        if (assignmentRequest.getDate().before(CalenderDate.getYesterdayUtil()))
            throw new InvalidDateException(ErrorMessage.DATE_IN_PAST);

        // VALIDATES THE CURRENT DATE TO MAKE SURE IT HAS NOT BEEN PASSED YET
        if (currentAssignmentDate.before(CalenderDate.getYesterdayUtil()))
            throw new InvalidDateException(ErrorMessage.DATE_TOO_OLD);

        VisitAssignmentMapper.update(foundAssignment, assignmentRequest);

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

    public ResponseEntity<VisitAssignmentResponse> createUnplannedVisit(Long id, UnplannedVisitRequest unplanned) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));
        VisitDefinition parentDefinition = foundAssignment.getVisitDefinition();

        GeoCoordinates geoCoordinates = GeoCoordinates
                .builder()
                .latitude(unplanned.getLatitude())
                .longitude(unplanned.getLongitude())
                .build();

        Customer customerToSave = Customer.builder()
                .name(unplanned.getName())
                .geoCoordinates(geoCoordinates)
                .location(parentDefinition.getLocation())
                .contacts(new ArrayList<>())
                .build();

        List<VisitType> contactTypeList = new ArrayList<>();
        contactTypeList.add(parentDefinition.getType());

        Contact contactToSave = Contact.builder()
                .firstName(unplanned.getFirstName())
                .lastName(unplanned.getLastName())
                .phoneNumber(PhoneNumberFormatter.formatPhone(unplanned.getPhoneNumber()))
                .email(unplanned.getEmail().isBlank() ? null : unplanned.getEmail().trim())
                .visitTypes(contactTypeList)
                .customer(customerToSave)
                .build();

        customerToSave.getContacts().add(contactToSave);
        customerToSave = customerRepository.save(customerToSave);

        return assignVisitToCustomer(id, customerToSave.getId());
    }

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

        Location assignmentLocation = foundAssignment.getVisitDefinition().getLocation();

        List<Contact> contactList = contactRepository
                .findContactsByCustomerAndVisitTypesContaining(foundCustomer, assignmentVisitType);

        if (!foundCustomer.getLocation().equals(assignmentLocation))
            throw new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_IN_CITY);

        if (contactList.isEmpty())
            throw new NoContactTypeException();

        if (foundAssignment.getDate().before(CalenderDate.getYesterdaySql()))
            throw new InvalidDateException(ErrorMessage.DATE_TOO_OLD);

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

    public ResponseEntity<?> createContactAndAssignCustomerToAssignment(Long assignmentId, Long customerId, ContactRequest contactRequest) {
        Customer foundCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getVisitTypes());
        Contact contactToSave = ContactMapper.toEntity(contactRequest, foundCustomer, visitTypes);

        foundCustomer.getContacts().add(contactToSave);
        customerRepository.save(foundCustomer);

        return assignVisitToCustomer(assignmentId, customerId);
    }

    public ResponseEntity<?> deleteCustomerFromAssignment(Long assignmentId, Long customerId) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findByIdAndEnabledTrue(assignmentId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findByIdAndEnabledTrue(customerId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        if (!foundAssignment.getCustomers().contains(foundCustomer))
            throw new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_ASSIGNED);

        System.out.println("LOOKING FOR FORM ===> ASSIGNMENT : " + foundAssignment.getId() + " | CUSTOMER : " + foundCustomer.getId());
        VisitForm foundCustomerForm = visitFormRepository.findByCustomerAndVisitAssignment(foundCustomer, foundAssignment)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));


        foundAssignment.getCustomers().remove(foundCustomer);
        foundAssignment.getVisitForms().remove(foundCustomerForm);

        visitFormRepository.delete(foundCustomerForm);
        foundAssignment = visitAssignmentRepository.save(foundAssignment);

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

            java.sql.Date nextAssignmentDate = CalenderDate.getDateWithOffsetSql(currentAssignment.getDate(), frequency);

            VisitAssignment nextAssignment = VisitAssignment.builder()
                    .visitDefinition(currentAssignment.getVisitDefinition())
                    .comment(currentAssignment.getComment())
                    .user(currentAssignment.getUser())
                    .customers(new ArrayList<>())
                    .status(VisitStatus.NOT_STARTED)
                    .date(nextAssignmentDate)
                    .nextVisitAssignment(null)
                    .build();

            currentAssignment.setNextVisitAssignment(nextAssignment);

            visitAssignmentRepository.save(nextAssignment);
            visitAssignmentRepository.save(currentAssignment);

            createNextAssignmentForm(currentCustomer, nextAssignment);

        } else {
            VisitAssignment nextAssignment = currentAssignment.getNextVisitAssignment();
            nextAssignment.setStatus(VisitStatus.UNDERGOING);

            createNextAssignmentForm(currentCustomer, nextAssignment);

            visitAssignmentRepository.save(nextAssignment);
        }

    }

    private boolean isSurvey(VisitDefinition visitDefinition) {
        return visitDefinition.getType().getName().equalsIgnoreCase("Survey");
    }

    private void createNextAssignmentForm(Customer currentCustomer, VisitAssignment currentAssignment) {
        if (currentAssignment.getCustomers().contains(currentCustomer))
            throw new CustomerAlreadyAssignedException();

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


}

