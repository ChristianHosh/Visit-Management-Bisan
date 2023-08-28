package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidStatusUpdateException;
import com.example.vm.controller.error.exception.LocationTooFarException;
import com.example.vm.dto.mapper.VisitFormMapper;
import com.example.vm.dto.request.AssignmentCustomerRequest;
import com.example.vm.dto.request.FormGeolocationRequest;
import com.example.vm.dto.response.VisitFormResponse;
import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitForm;
import com.example.vm.model.VisitType;
import com.example.vm.repository.ContactRepository;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.VisitAssignmentRepository;
import com.example.vm.repository.VisitFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class VisitFormService {

    private final VisitFormRepository visitFormRepository;
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final VisitAssignmentService visitAssignmentService;

    @Autowired
    public VisitFormService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, VisitAssignmentService visitAssignmentService) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.visitAssignmentService = visitAssignmentService;
    }

    public ResponseEntity<VisitFormResponse> findVisitFormById(Long id) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<VisitFormResponse> createNewForm(AssignmentCustomerRequest assignmentCustomerRequest) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(assignmentCustomerRequest.getAssignmentId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(assignmentCustomerRequest.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        if (!foundAssignment.getCustomers().contains(foundCustomer))
            throw new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_ASSIGNED);


        VisitForm newVisitForm = VisitForm.builder()
                .startTime(Timestamp.from(Instant.now()))
                .status(VisitStatus.UNDERGOING)
                .customer(foundCustomer)
                .visitAssignment(foundAssignment)
                .build();


        VisitType visitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contacts = contactRepository.findContactsByCustomerAndVisitTypesContaining(foundCustomer, visitType);

        newVisitForm.setContacts(contacts);
        newVisitForm = visitFormRepository.save(newVisitForm);

        return ResponseEntity.ok(VisitFormMapper.toListResponse(newVisitForm));

    }

    public ResponseEntity<VisitFormResponse> completeForm(Long id, FormGeolocationRequest formGeolocationRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        validateDistance(formGeolocationRequest, foundForm.getCustomer().getAddress());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.UNDERGOING))
            throw new InvalidStatusUpdateException();


        foundForm.setStatus(VisitStatus.COMPLETED);
        foundForm.setEndTime(Timestamp.from(Instant.now()));
        foundForm.setNote(formGeolocationRequest.getNote());

        foundForm = visitFormRepository.save(foundForm);

        visitAssignmentService.createNextVisitAssignment(foundForm.getVisitAssignment(), foundForm.getCustomer());

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }


    public ResponseEntity<VisitFormResponse> startForm(Long id, FormGeolocationRequest formGeolocationRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        validateDistance(formGeolocationRequest, foundForm.getCustomer().getAddress());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.NOT_STARTED))
            throw new InvalidStatusUpdateException();

        foundForm.setStatus(VisitStatus.UNDERGOING);
        foundForm.setStartTime(Timestamp.from(Instant.now()));

        foundForm = visitFormRepository.save(foundForm);

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    private static void validateDistance(FormGeolocationRequest formGeolocationRequest, Address customerAddress) {
        double distance = distanceBetweenTwoPoints(customerAddress, formGeolocationRequest);

        if (distance > 250)
            throw new LocationTooFarException();
    }

    private static double distanceBetweenTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2)) * 111 * 1000;
    }

    private static double distanceBetweenTwoPoints(Address customerAddress, FormGeolocationRequest formGeolocationRequest) {
        double customerLat = customerAddress.getLatitude();
        double customerLng = customerAddress.getLongitude();

        double userLat = formGeolocationRequest.getLatitude();
        double userLng = formGeolocationRequest.getLongitude();

        return distanceBetweenTwoPoints(customerLat, customerLng, userLat, userLng);
    }


}
