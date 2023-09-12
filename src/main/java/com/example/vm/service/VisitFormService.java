package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidStatusUpdateException;
import com.example.vm.controller.error.exception.LocationTooFarException;
import com.example.vm.dto.mapper.ContactMapper;
import com.example.vm.dto.mapper.VisitFormMapper;
import com.example.vm.dto.request.AssignmentCustomerRequest;
import com.example.vm.dto.request.form.FormPaymentRequest;
import com.example.vm.dto.request.form.FormRequest;
import com.example.vm.dto.request.form.FormAnswerRequest;
import com.example.vm.dto.response.VisitFormResponse;
import com.example.vm.model.*;
import com.example.vm.model.enums.PaymentType;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.enums.VisitTypeBase;
import com.example.vm.model.templates.PaymentReceipt;
import com.example.vm.model.templates.PaymentReceiptRepository;
import com.example.vm.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    private final PaymentReceiptRepository paymentReceiptRepository;

    @Autowired
    public VisitFormService(VisitFormRepository visitFormRepository, VisitAssignmentRepository visitAssignmentRepository, CustomerRepository customerRepository, ContactRepository contactRepository, VisitAssignmentService visitAssignmentService,
                            PaymentReceiptRepository paymentReceiptRepository) {
        this.visitFormRepository = visitFormRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.visitAssignmentService = visitAssignmentService;
        this.paymentReceiptRepository = paymentReceiptRepository;
    }

    public ResponseEntity<VisitFormResponse> findVisitFormById(Long id) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<VisitFormResponse> createNewUnplannedCustomerForm(AssignmentCustomerRequest assignmentCustomerRequest) {
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

    public ResponseEntity<VisitFormResponse> completePaymentForm(VisitForm form, FormPaymentRequest formPaymentRequest) {
        PaymentReceipt receipt = PaymentReceipt.builder()
                .amount(formPaymentRequest.getAmount())
                .paymentType(PaymentType.valueOf(formPaymentRequest.getType()))
                .visitForm(form)
                .build();


        paymentReceiptRepository.save(receipt);
        form = visitFormRepository.save(form);

        visitAssignmentService.createNextVisitAssignment(form.getVisitAssignment(), form.getCustomer());

        return ResponseEntity.ok(VisitFormMapper.toListResponse(form));
    }

    public ResponseEntity<?> completeForm(Long id, FormRequest formRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        if (!foundForm.getStatus().equals(VisitStatus.UNDERGOING))
            throw new InvalidStatusUpdateException();

        validateDistance(formRequest, foundForm.getCustomer());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        foundForm.setStatus(VisitStatus.COMPLETED);
        foundForm.setEndTime(Timestamp.from(Instant.now()));
        foundForm.setNote(formRequest.getNote());
        if (foundForm.getGeoCoordinates() == null)
            foundForm.setGeoCoordinates(new GeoCoordinates());

        foundForm.getGeoCoordinates().setLongitude(formRequest.getLongitude());
        foundForm.getGeoCoordinates().setLatitude(formRequest.getLatitude());
        completeAssignmentIfAllFormsCompleted(foundForm);

        VisitType visitDefinitionType = foundForm.getVisitAssignment().getVisitDefinition().getType();
        if (formRequest instanceof FormAnswerRequest && visitDefinitionType.getBase().equals(VisitTypeBase.QUESTION)) {
            System.out.println("INSTANCE OF SURVEY");
            return completeAnswerForm(foundForm, (FormAnswerRequest) formRequest);
        } else if (formRequest instanceof FormPaymentRequest && visitDefinitionType.getBase().equals(VisitTypeBase.PAYMENT)) {
            System.out.println("INSTANCE OF COLLECTION");
            return completePaymentForm(foundForm, (FormPaymentRequest) formRequest);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("SOMETHING WENT WRONG");
    }

    public ResponseEntity<VisitFormResponse> completeAnswerForm(VisitForm foundForm, FormAnswerRequest formAnswerRequest) {


        foundForm = visitFormRepository.save(foundForm);


        visitAssignmentService.createNextVisitAssignment(foundForm.getVisitAssignment(), foundForm.getCustomer());

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<VisitFormResponse> startForm(Long id, FormRequest formRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        validateDistance(formRequest, foundForm.getCustomer());
        // THROWS AN LOCATION_TOO_FAR EXCEPTION

        if (!foundForm.getStatus().equals(VisitStatus.NOT_STARTED))
            throw new InvalidStatusUpdateException();

        foundForm.setStatus(VisitStatus.UNDERGOING);
        foundForm.setStartTime(Timestamp.from(Instant.now()));
        if (foundForm.getGeoCoordinates() == null)
            foundForm.setGeoCoordinates(new GeoCoordinates());

        foundForm.getGeoCoordinates().setLatitude(formRequest.getLatitude());
        foundForm.getGeoCoordinates().setLongitude(formRequest.getLongitude());

        VisitAssignment parentAssignment = foundForm.getVisitAssignment();
        parentAssignment.setStatus(VisitStatus.UNDERGOING);

        visitAssignmentRepository.save(parentAssignment);
        foundForm = visitFormRepository.save(foundForm);

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<?> cancelForm(Long id, FormRequest formRequest) {
        VisitForm foundForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        if (foundForm.getStatus().equals(VisitStatus.COMPLETED))
            throw new InvalidStatusUpdateException();

        foundForm.setStatus(VisitStatus.CANCELED);
        if (foundForm.getGeoCoordinates() == null)
            foundForm.setGeoCoordinates(new GeoCoordinates());
        foundForm.getGeoCoordinates().setLatitude(formRequest.getLatitude());
        foundForm.getGeoCoordinates().setLongitude(formRequest.getLongitude());

        foundForm = visitFormRepository.save(foundForm);

        return ResponseEntity.ok(VisitFormMapper.toListResponse(foundForm));
    }

    public ResponseEntity<?> findFormContactsByFormId(Long id) {
        VisitForm foundVisitForm = visitFormRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.FORM_NOT_FOUND));

        List<Contact> formContactList = foundVisitForm.getContacts();

        return ResponseEntity.ok(ContactMapper.listToResponseList(formContactList));
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

    private static void validateDistance(FormRequest formRequest, Customer customer) {
        double distance = distanceBetweenTwoPoints(customer.getGeoCoordinates(), formRequest);

        if (distance > 250)
            throw new LocationTooFarException();
    }

    private static double distanceBetweenTwoPoints(double lat1, double lng1, double lat2, double lng2) {
        return Math.sqrt(Math.pow(lat2 - lat1, 2) + Math.pow(lng2 - lng1, 2)) * 111 * 1000;
    }

    private static double distanceBetweenTwoPoints(GeoCoordinates coordinates, FormRequest formSurveyRequest) {
        double customerLat = coordinates.getLatitude();
        double customerLng = coordinates.getLongitude();

        double userLat = formSurveyRequest.getLatitude();
        double userLng = formSurveyRequest.getLongitude();

        return distanceBetweenTwoPoints(customerLat, customerLng, userLat, userLng);
    }


}
