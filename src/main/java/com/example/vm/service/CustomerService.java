package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.LocationNotFoundException;
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
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private static final String GEOLOCATION_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

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

        if (isNotPreciseLocation(customerRequest)) {
            try {
                Double[] geolocation = getGeolocation(foundLocation);
                customerToSave.setLatitude(geolocation[0]);
                customerToSave.setLongitude(geolocation[1]);
            } catch (IOException | InterruptedException | ApiException e) {
                throw new RuntimeException(e);
            } catch (LocationNotFoundException e) {
                throw new LocationNotFoundException();
            }
        }

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

        if (isNotPreciseLocation(customerRequest)) {
            try {
                Double[] geolocation = getGeolocation(foundLocation);
                customerToUpdate.setLatitude(geolocation[0]);
                customerToUpdate.setLongitude(geolocation[1]);
            } catch (IOException | InterruptedException | ApiException e) {
                throw new RuntimeException(e);
            } catch (LocationNotFoundException e) {
                throw new LocationNotFoundException();
            }
        }

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

    private Double[] getGeolocation(Location location) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException, LocationNotFoundException {
        try (GeoApiContext context = new GeoApiContext.Builder().apiKey(GEOLOCATION_KEY).build()) {
            GeocodingResult[] geocodingResults = GeocodingApi
                    .geocode(context, location.getAddress() + " " + location.getCity().getName())
                    .await();

            if (geocodingResults.length == 0)
                throw new LocationNotFoundException();

            return new Double[]{
                    geocodingResults[0].geometry.location.lat,
                    geocodingResults[0].geometry.location.lng,
            };
        }
    }

    private boolean isNotPreciseLocation(CustomerRequest customerRequest) {
        return customerRequest.getLongitude() == 0.0 && customerRequest.getLatitude() == 0.0;
    }


}
