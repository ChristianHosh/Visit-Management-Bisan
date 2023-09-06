package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.ContactMapper;
import com.example.vm.dto.mapper.CustomerMapper;
import com.example.vm.dto.mapper.VisitAssignmentMapper;
import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.request.CustomerRequest;
import com.example.vm.dto.response.ContactResponse;
import com.example.vm.dto.response.CustomerResponse;
import com.example.vm.model.*;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.LocationRepository;
import com.example.vm.repository.VisitAssignmentRepository;
import com.example.vm.service.util.CalenderDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final VisitTypeService visitTypeService;
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, VisitTypeService visitTypeService,
                           VisitAssignmentRepository visitAssignmentRepository,
                           LocationRepository locationRepository) {
        this.customerRepository = customerRepository;
        this.visitTypeService = visitTypeService;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.locationRepository = locationRepository;
    }

    public ResponseEntity<List<CustomerResponse>> findAllCustomers() {
        List<Customer> queryResult = customerRepository.findAll();

        return ResponseEntity.ok(CustomerMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<CustomerResponse>> findAllCustomersWhoHasAssignment() {
        List<Customer> queryResult = customerRepository.findCustomersByVisitAssignmentsIsNotEmpty();

        return ResponseEntity.ok(CustomerMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<CustomerResponse>> findAllEnabledCustomers() {
        List<Customer> queryResult = customerRepository.findCustomerByEnabledTrue();

        return ResponseEntity.ok(CustomerMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<CustomerResponse> findCustomerById(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        return ResponseEntity.ok(CustomerMapper.toDetailedResponse(foundCustomer));
    }

    public ResponseEntity<List<CustomerResponse>> searchByQuery(String query) {
        List<Customer> queryResult = customerRepository.findByNameContainsIgnoreCaseAndLocation_AddressContainsIgnoreCaseAndLocation_City_NameContainsIgnoreCase(query, query, query);

        return ResponseEntity.ok(CustomerMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<ContactResponse>> getCustomerContacts(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        return ResponseEntity.ok(ContactMapper.listToResponseList(foundCustomer.getContacts()));
    }

    public ResponseEntity<Map<String, Object>> saveNewCustomer(CustomerRequest customerRequest) {
        Location foundLocation = locationRepository.findById(customerRequest.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));

        Customer customerToSave = CustomerMapper.toEntity(customerRequest, foundLocation);

        customerToSave = customerRepository.save(customerToSave);

        List<VisitDefinition> visitDefinitionList = foundLocation.getVisitDefinitions();
        List<VisitAssignment> availableAssignments = new ArrayList<>();

        visitDefinitionList
                .forEach(currentDefinition -> availableAssignments
                        .addAll(visitAssignmentRepository
                                .findByVisitDefinitionAndDateAfter(currentDefinition, CalenderDate.getYesterdaySql())));


        Map<String, Object> map = new HashMap<>();
        map.put("customer", CustomerMapper.toListResponse(customerToSave));
        map.put("availableAssignments", VisitAssignmentMapper.listToResponseList(availableAssignments));

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    public ResponseEntity<CustomerResponse> saveContactToCustomer(Long id, ContactRequest contactRequest) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getVisitTypes());

        Contact contactToSave = ContactMapper.toEntity(contactRequest, foundCustomer, visitTypes);

        foundCustomer.getContacts().add(contactToSave);

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.toDetailedResponse(foundCustomer));
    }

    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        Location foundLocation = locationRepository.findById(customerRequest.getLocationId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));

        CustomerMapper.update(customerToUpdate, customerRequest, foundLocation);

        customerToUpdate = customerRepository.save(customerToUpdate);

        return ResponseEntity.ok(CustomerMapper.toDetailedResponse(customerToUpdate));
    }

    public ResponseEntity<CustomerResponse> enableCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        foundCustomer.setEnabled(!foundCustomer.getEnabled());
        boolean isEnabled = foundCustomer.getEnabled();
        foundCustomer.getContacts()
                .forEach(contact -> contact.setEnabled(isEnabled));


        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.ok(CustomerMapper.toDetailedResponse(foundCustomer));
    }


}
