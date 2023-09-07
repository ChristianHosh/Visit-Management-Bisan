package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidStatusUpdateException;
import com.example.vm.controller.error.exception.LocationTooFarException;
import com.example.vm.dto.mapper.ContactMapper;
import com.example.vm.dto.mapper.VisitFormMapper;
import com.example.vm.dto.request.AssignmentCustomerRequest;
import com.example.vm.dto.request.FormUpdateRequest;
import com.example.vm.dto.response.VisitFormResponse;
import com.example.vm.model.*;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.templates.SurveyAnswers;
import com.example.vm.repository.SurveyAnswersRepository;
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
    private final SurveyAnswersRepository surveyAnswersRepository;

    @Autowired
    public VisitFormService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, VisitAssignmentService visitAssignmentService,
                            SurveyAnswersRepository surveyAnswersRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.visitAssignmentService = visitAssignmentService;
        this.surveyAnswersRepository = surveyAnswersRepository;
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

    public ResponseEntity<VisitFormResponse> completeForm(Long id, FormUpdateRequest formUpdateRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        validateDistance(formUpdateRequest, foundForm.getCustomer());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.UNDERGOING))
            throw new InvalidStatusUpdateException();


        foundForm.setStatus(VisitStatus.COMPLETED);
        foundForm.setEndTime(Timestamp.from(Instant.now()));
        foundForm.setNote(formUpdateRequest.getNote());
        if (foundForm.getGeoCoordinates() == null)
            foundForm.setGeoCoordinates(new GeoCoordinates());

        foundForm.getGeoCoordinates().setLongitude(formUpdateRequest.getLongitude());
        foundForm.getGeoCoordinates().setLatitude(formUpdateRequest.getLatitude());

        SurveyAnswers surveyAnswers = SurveyAnswers
                .builder()
                .answer1(formUpdateRequest.getAnswer1())
                .answer2(formUpdateRequest.getAnswer2())
                .answer3(formUpdateRequest.getAnswer3())
                .visitForm(foundForm)
                .build();

        completeAssignmentIfAllFormsCompleted(foundForm);
        foundForm = visitFormRepository.save(foundForm);

        surveyAnswersRepository.save(surveyAnswers);

        visitAssignmentService.createNextVisitAssignment(foundForm.getVisitAssignment(), foundForm.getCustomer());

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    private void completeAssignmentIfAllFormsCompleted(VisitForm foundForm) {
        VisitAssignment parentAssignment = foundForm.getVisitAssignment();
        boolean areAllFormsCompleted = parentAssignment
                .getVisitForms()
                .stream()
                .allMatch(visitForm -> visitForm.getStatus().equals(VisitStatus.COMPLETED));

        parentAssignment.setStatus(areAllFormsCompleted ? VisitStatus.COMPLETED : VisitStatus.UNDERGOING);
        visitAssignmentRepository.save(parentAssignment);
    }

    public ResponseEntity<VisitFormResponse> startForm(Long id, FormUpdateRequest formUpdateRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        validateDistance(formUpdateRequest, foundForm.getCustomer());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.NOT_STARTED))
            throw new InvalidStatusUpdateException();

        foundForm.setStatus(VisitStatus.UNDERGOING);
        foundForm.setStartTime(Timestamp.from(Instant.now()));
        if (foundForm.getGeoCoordinates() == null)
            foundForm.setGeoCoordinates(new GeoCoordinates());

        foundForm.getGeoCoordinates().setLatitude(formUpdateRequest.getLatitude());
        foundForm.getGeoCoordinates().setLongitude(formUpdateRequest.getLongitude());

        VisitAssignment parentAssignment = foundForm.getVisitAssignment();
        parentAssignment.setStatus(VisitStatus.UNDERGOING);

        visitAssignmentRepository.save(parentAssignment);
        foundForm = visitFormRepository.save(foundForm);

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<?> cancelForm(Long id, FormUpdateRequest formUpdateRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        if (foundForm.getStatus().equals(VisitStatus.COMPLETED))
            throw new InvalidStatusUpdateException();

        foundForm.setStatus(VisitStatus.CANCELED);
        if (foundForm.getGeoCoordinates() == null)
            foundForm.setGeoCoordinates(new GeoCoordinates());

        foundForm.getGeoCoordinates().setLatitude(formUpdateRequest.getLatitude());
        foundForm.getGeoCoordinates().setLongitude(formUpdateRequest.getLongitude());

        foundForm = visitFormRepository.save(foundForm);

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<?> findFormContactsByFormId(Long id) {
        VisitForm foundVisitForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        List<Contact> formContactList = foundVisitForm.getContacts();

        return ResponseEntity.ok(ContactMapper.listToResponseList(formContactList));
    }

    private static void validateDistance(FormUpdateRequest formUpdateRequest, Customer customer) {
        double distance = distanceBetweenTwoPoints(customer.getGeoCoordinates(), formUpdateRequest);

        System.out.println("COMPARING LOCATION: \n" +
                "CUSTOMER:\n" +
                "\t GEO: \" + " + customer.getGeoCoordinates().getLatitude() + ", " + customer.getGeoCoordinates().getLongitude() +
                "\nUSER:\n" +
                "\t GEO: \" + " + formUpdateRequest.getLatitude() + ", " + formUpdateRequest.getLongitude() +
                "\n DST: " + distance);

        if (distance > 250)
            throw new LocationTooFarException();
    }

    private static double distanceBetweenTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2)) * 111 * 1000;
    }

    private static double distanceBetweenTwoPoints(GeoCoordinates coordinates, FormUpdateRequest formUpdateRequest) {
        double customerLat = coordinates.getLatitude();
        double customerLng = coordinates.getLongitude();

        double userLat = formUpdateRequest.getLatitude();
        double userLng = formUpdateRequest.getLongitude();

        return distanceBetweenTwoPoints(customerLat, customerLng, userLat, userLng);
    }


}
