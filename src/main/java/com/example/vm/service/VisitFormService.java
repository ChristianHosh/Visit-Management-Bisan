package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.AssignmentCustomerDTO;
import com.example.vm.dto.LngLatDTO;
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

    @Autowired
    public VisitFormService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
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

        Timestamp timestamp = Timestamp.from(Instant.now());

        VisitForm newVisitForm = VisitForm.builder()
                .startTime(timestamp)
                .status(VisitStatus.UNDERGOING)
                .customer(foundCustomer)
                .visitAssignment(foundAssignment)
                .build();

        newVisitForm.setCreatedTime(timestamp);
        newVisitForm.setLastModifiedTime(timestamp);

        VisitType visitType = foundAssignment.getVisitDefinition().getType();

        List<Contact> contacts = contactRepository.findContactsByCustomerAndVisitTypesContaining(foundCustomer, visitType);

        newVisitForm.setContacts(contacts);
        newVisitForm = visitFormRepository.save(newVisitForm);

        return ResponseEntity.ok(newVisitForm.toDetailPayload());

    }

    public ResponseEntity<VisitFormDetailPayload> updateFormStatusComplete(UUID id, LngLatDTO geolocation) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.FORM_NOT_FOUND));

        System.out.println(foundForm.getCustomer().getName());

        double customerLng = foundForm.getCustomer().getAddress().getLongitude();
        double customerLat = foundForm.getCustomer().getAddress().getLatitude();
        System.out.println("CUSTOMER: " + customerLat + " | " + customerLng);

        double userLng = foundForm.getCustomer().getAddress().getLongitude();
        double userLat = foundForm.getCustomer().getAddress().getLatitude();
        System.out.println("USER: " + userLat + " | " + userLng);

        double distance = distanceBetweenTwoPoints(customerLat, customerLng, userLat, userLng);

        System.out.println("DISTANCE : " + distance);

        return null;
    }


    public ResponseEntity<VisitFormDetailPayload> updateFormStatusCancel(UUID id) {
        return null;
    }

    private static double distanceBetweenTwoPoints(double lat1, double lng1, double lat2, double lng2) {
       /* double earthRadiusInMeters = 6371000;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadiusInMeters * c;*/
        //     return Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lng2 - lng1)) * 6371 *1000;
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2)) * 111 * 1000;

    }

}
