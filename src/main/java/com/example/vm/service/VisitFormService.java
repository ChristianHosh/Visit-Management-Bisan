package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidStatusUpdateException;
import com.example.vm.controller.error.exception.LocationTooFarException;
import com.example.vm.dto.AssignmentCustomerDTO;
import com.example.vm.dto.FormGeolocationDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitForm;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitFormDetailPayload;
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
import java.util.UUID;

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

    public ResponseEntity<VisitFormDetailPayload> findVisitFormById(UUID id) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.FORM_NOT_FOUND));

        return ResponseEntity.ok(foundForm.toDetailPayload());
    }

    public ResponseEntity<VisitFormDetailPayload> createNewForm(AssignmentCustomerDTO assignmentCustomerRequest) {
        VisitAssignment foundAssignment = visitAssignmentRepository.findById(assignmentCustomerRequest.getAssignmentId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.ASSIGNMENT_NOT_FOUND));

        Customer foundCustomer = customerRepository.findById(assignmentCustomerRequest.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        if (!foundAssignment.getCustomers().contains(foundCustomer))
            throw new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_ASSIGNED);


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

        return ResponseEntity.ok(newVisitForm.toDetailPayload());

    }

    public ResponseEntity<VisitFormDetailPayload> completeForm(UUID id, FormGeolocationDTO formGeolocationDTO) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.FORM_NOT_FOUND));

        validateDistance(formGeolocationDTO, foundForm.getCustomer().getAddress());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.UNDERGOING))
            throw new InvalidStatusUpdateException();


        foundForm.setStatus(VisitStatus.COMPLETED);
        foundForm.setEndTime(Timestamp.from(Instant.now()));
        foundForm.setNote(formGeolocationDTO.getNote());

        foundForm = visitFormRepository.save(foundForm);

        visitAssignmentService.createNextVisitAssignment(foundForm.getVisitAssignment(), foundForm.getCustomer());

        return ResponseEntity.ok(foundForm.toDetailPayload());
    }



    public ResponseEntity<VisitFormDetailPayload> startForm(UUID id, FormGeolocationDTO formGeolocationDTO) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.FORM_NOT_FOUND));

        validateDistance(formGeolocationDTO, foundForm.getCustomer().getAddress());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.NOT_STARTED))
            throw new InvalidStatusUpdateException();

        foundForm.setStatus(VisitStatus.UNDERGOING);
        foundForm.setStartTime(Timestamp.from(Instant.now()));

        foundForm = visitFormRepository.save(foundForm);

        return ResponseEntity.ok(foundForm.toDetailPayload());
    }

    private static void validateDistance(FormGeolocationDTO formGeolocationDTO, Address customerAddress) {
        double distance = distanceBetweenTwoPoints(customerAddress, formGeolocationDTO);

        double maxDistance = 250;

        if (customerAddress.getIsPrecise())
            maxDistance = 25;

        if (distance > maxDistance)
            throw new LocationTooFarException();
    }

    private static double distanceBetweenTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2)) * 111 * 1000;
    }

    private static double distanceBetweenTwoPoints(Address customerAddress, FormGeolocationDTO formGeolocationDTO) {
        double customerLat = customerAddress.getLatitude();
        double customerLng = customerAddress.getLongitude();

        double userLat = formGeolocationDTO.getLatitude();
        double userLng = formGeolocationDTO.getLongitude();

        return distanceBetweenTwoPoints(customerLat, customerLng, userLat, userLng);
    }

    
}