package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.InvalidDateException;
import com.example.vm.dto.mapper.VisitAssignmentMapper;
import com.example.vm.dto.mapper.VisitDefinitionMapper;
import com.example.vm.dto.request.VisitAssignmentRequest;
import com.example.vm.dto.request.VisitDefinitionRequest;
import com.example.vm.dto.response.VisitDefinitionResponse;
import com.example.vm.model.*;
import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.enums.VisitTypeBase;
import com.example.vm.model.templates.QuestionTemplate;
import com.example.vm.repository.*;
import com.example.vm.service.util.CalenderDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VisitDefinitionService {
    private final VisitDefinitionRepository visitDefinitionRepository;
    private final VisitTypeRepository visitTypeRepository;
    private final LocationRepository locationRepository;
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final CustomerRepository customerRepository;
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;
    private final QuestionTemplateRepository questionTemplateRepository;

    @Autowired
    public VisitDefinitionService(VisitDefinitionRepository visitDefinitionRepository, VisitTypeRepository visitTypeRepository,
                                  LocationRepository locationRepository, VisitAssignmentRepository visitAssignmentRepository,
                                  CustomerRepository customerRepository,
                                  ContactRepository contactRepository,
                                  UserRepository userRepository,
                                  QuestionTemplateRepository questionTemplateRepository) {
        this.visitDefinitionRepository = visitDefinitionRepository;
        this.visitTypeRepository = visitTypeRepository;
        this.locationRepository = locationRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.customerRepository = customerRepository;
        this.contactRepository = contactRepository;
        this.userRepository = userRepository;
        this.questionTemplateRepository = questionTemplateRepository;
    }

    public ResponseEntity<List<VisitDefinitionResponse>> findAllVisitDefinition() {
        List<VisitDefinition> queryResult = visitDefinitionRepository.findAll();

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<VisitDefinitionResponse>> findAllEnabledVisitDefinitions() {
        List<VisitDefinition> queryResult = visitDefinitionRepository.findVisitDefinitionsByEnabledTrue();

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<VisitDefinitionResponse> findVisitDefinitionByID(Long id) {
        VisitDefinition foundVisitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository.
                findByVisitDefinitionAndDateAfter(foundVisitDefinition, CalenderDate.getYesterdaySql());

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(foundVisitDefinition, visitAssignmentList));
    }

    public ResponseEntity<VisitDefinitionResponse> saveNewVisitDefinition(VisitDefinitionRequest visitDefinitionRequest) {
        VisitType foundVisitType = visitTypeRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        Location foundLocation = locationRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        VisitDefinition visitDefinitionToSave = VisitDefinitionMapper.toEntity(visitDefinitionRequest, foundVisitType, foundLocation);

        if (foundVisitType.getBase().equals(VisitTypeBase.QUESTION))
            createQuestionTemplate(visitDefinitionRequest, visitDefinitionToSave);


        visitDefinitionToSave = visitDefinitionRepository.save(visitDefinitionToSave);

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(visitDefinitionToSave));
    }

    public ResponseEntity<VisitDefinitionResponse> updateVisitDefinition(Long id, VisitDefinitionRequest visitDefinitionRequest) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        VisitType foundVisitType = visitTypeRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getTypeId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TYPE_NOT_FOUND));

        Location foundlocation = locationRepository.findByIdAndEnabledTrue(visitDefinitionRequest.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));


        VisitDefinitionMapper.update(foundDefinition, visitDefinitionRequest, foundVisitType, foundlocation);

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(foundDefinition));
    }

    public ResponseEntity<VisitDefinitionResponse> enableVisitDefinition(Long id) {
        VisitDefinition foundDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        foundDefinition.setEnabled(!foundDefinition.getEnabled());

        boolean isEnabled = foundDefinition.getEnabled();

        foundDefinition.getVisitAssignments()
                .forEach(visitAssignment -> visitAssignment.setEnabled(isEnabled));

        foundDefinition = visitDefinitionRepository.save(foundDefinition);

        return ResponseEntity.ok(VisitDefinitionMapper.toDetailedResponse(foundDefinition));

    }

    public ResponseEntity<?> searchVisitDefinitions(String name, Boolean enabled, Boolean recurring, Long typeId, Long cityId, Long locationId) {
        List<VisitDefinition> visitDefinitionList = visitDefinitionRepository.searchVisitDefinitions(name, enabled, recurring, typeId, cityId, locationId);

        return ResponseEntity.ok(VisitDefinitionMapper.listToResponseList(visitDefinitionList));
    }

    public ResponseEntity<VisitDefinitionResponse> saveNewVisitAssignmentToDefinition(Long id, VisitAssignmentRequest visitAssignmentRequest) {
        VisitDefinition foundVisitDefinition = visitDefinitionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.DEFINITION_NOT_FOUND));

        User userToAssign = userRepository.findById(visitAssignmentRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        // VALIDATES THE DATE TO MAKE SURE IT IN THE TODAY OR IN THE FUTURE
        if (visitAssignmentRequest.getDate().before(CalenderDate.getYesterdayUtil()))
            throw new InvalidDateException(ErrorMessage.DATE_IN_PAST);

        VisitAssignment visitAssignmentToSave = VisitAssignmentMapper.toEntity(visitAssignmentRequest, foundVisitDefinition, userToAssign);
        // CREATES THE VISIT ASSIGNMENT ENTITY

        List<Customer> availableCustomersInLocation = customerRepository.findCustomerByEnabledTrueAndLocation(foundVisitDefinition.getLocation());
        // LIST OF ALL CUSTOMERS THAT ARE IN THE ASSIGNMENT'S LOCATION

        List<VisitForm> newVisitForms = new ArrayList<>();
        VisitDefinition definitionToCheck = foundVisitDefinition;
        availableCustomersInLocation = availableCustomersInLocation
                .stream()
                .filter(customer -> {
                    List<Contact> customerContacts = contactRepository.findContactsByCustomerAndVisitTypesContaining(customer, definitionToCheck.getType());
                    // LIST OF CUSTOMER'S CONTACTS WHO HAVE THE SAME ASSIGNMENT'S TYPE
                    // IF LIST IS EMPTY THEN WE CAN'T ASSIGN THIS CUSTOMER TO THE ASSIGNMENT
                    // WE REMOVE THE CUSTOMER FROM THE AVAILABLE CUSTOMER LIST
                    if (customerContacts.isEmpty())
                        return false;

                    // CREATES THE NEW FORM FOR THAT CUSTOMER
                    VisitForm newForm = VisitForm.builder()
                            .status(VisitStatus.NOT_STARTED)
                            .customer(customer)
                            .contacts(customerContacts)
                            .visitAssignment(visitAssignmentToSave)
                            .build();

                    // ADD THE NEW FORM TO THE LIST OF NEW FORMS TO ADD TO THE ASSIGNMENT
                    return newVisitForms.add(newForm);
                })
                .toList();

        visitAssignmentToSave.getCustomers().addAll(availableCustomersInLocation);
        visitAssignmentToSave.getVisitForms().addAll(newVisitForms);

        foundVisitDefinition.getVisitAssignments().add(visitAssignmentToSave);

        foundVisitDefinition = visitDefinitionRepository.save(foundVisitDefinition);

        return ResponseEntity.status(HttpStatus.CREATED).body(VisitDefinitionMapper.toDetailedResponse(foundVisitDefinition));
    }

    private void createQuestionTemplate(VisitDefinitionRequest visitDefinitionRequest, VisitDefinition visitDefinitionToSave) {
        QuestionTemplate questionTemplate = QuestionTemplate.builder()
                .question1(visitDefinitionRequest.getQuestion1())
                .question2(visitDefinitionRequest.getQuestion2())
                .question3(visitDefinitionRequest.getQuestion3())
                .visitDefinition(visitDefinitionToSave)
                .build();

        questionTemplateRepository.save(questionTemplate);
    }

}
