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
import com.example.vm.repository.CityRepository;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.VisitAssignmentRepository;
import com.example.vm.service.formatter.PhoneNumberFormatter;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CustomerService {

    private static final String GEOLOCATION_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final VisitTypeService visitTypeService;
    private final VisitAssignmentRepository visitAssignmentRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CityRepository cityRepository, VisitTypeService visitTypeService,
                           VisitAssignmentRepository visitAssignmentRepository) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.visitTypeService = visitTypeService;
        this.visitAssignmentRepository = visitAssignmentRepository;
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
        List<Customer> queryResult = customerRepository
                .findByNameContainsIgnoreCaseOrAddress_AddressLine1ContainsIgnoreCaseOrAddress_AddressLine2ContainsIgnoreCaseOrAddress_City_NameContainsIgnoreCase(query,query,query,query);

        return ResponseEntity.ok(CustomerMapper.listToResponseList(queryResult));
    }

    public ResponseEntity<List<ContactResponse>> getCustomerContacts(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        return ResponseEntity.ok(ContactMapper.listToResponseList(foundCustomer.getContacts()));
    }

    public ResponseEntity<Map<String, Object>> saveNewCustomer(CustomerRequest customerRequest) {
        City foundCity = cityRepository.findById(customerRequest.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        Customer customerToSave = Customer.builder()
                .name(customerRequest.getName())
                .visitAssignments(new ArrayList<>())
                .address(Address.builder()
                        .addressLine1(customerRequest.getAddressLine1())
                        .addressLine2(customerRequest.getAddressLine2())
                        .longitude(customerRequest.getLongitude())
                        .latitude(customerRequest.getLatitude())
                        .zipcode(customerRequest.getZipcode())
                        .city(foundCity)
                        .build())
                .build();

        if (isNotPreciseLocation(customerRequest)) {
            try {
                Double[] geolocation = getGeolocation(customerRequest, foundCity.getName());
                customerToSave.getAddress().setLatitude(geolocation[0]);
                customerToSave.getAddress().setLongitude(geolocation[1]);
            } catch (IOException | InterruptedException | ApiException e) {
                throw new RuntimeException(e);
            } catch (LocationNotFoundException e) {
                throw new LocationNotFoundException();
            }
        }

        customerToSave = customerRepository.save(customerToSave);

        Date todayDate = new Date(System.currentTimeMillis());

        List<VisitDefinition> visitDefinitionList = foundCity.getVisitDefinitions();

        List<VisitAssignment> availableAssignments = new ArrayList<>();

        for (VisitDefinition currentVisitDefinition : visitDefinitionList) {
            List<VisitAssignment> visitAssignments = visitAssignmentRepository
                    .findByVisitDefinitionAndDateAfter(currentVisitDefinition, todayDate);

            availableAssignments.addAll(visitAssignments);
        }

        Map<String, Object> map = new HashMap<>();
        map.put("customer", CustomerMapper.toListResponse(customerToSave));
        map.put("availableAssignments", VisitAssignmentMapper.listToResponseList(availableAssignments));

        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    public ResponseEntity<CustomerResponse> saveContactToCustomer(Long id, ContactRequest contactRequest) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getVisitTypes());

        String formattedNumber = PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber());

        Contact newContact = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .email(contactRequest.getEmail())
                .phoneNumber(formattedNumber)
                .visitTypes(visitTypes)
                .build();

        foundCustomer.getContacts().add(newContact);
        newContact.setCustomer(foundCustomer);

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(CustomerMapper.toDetailedResponse(foundCustomer));
    }

    public ResponseEntity<CustomerResponse> updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CUSTOMER_NOT_FOUND));

        City foundCity = cityRepository.findById(customerRequest.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        customerToUpdate.setName(customerRequest.getName());

        customerToUpdate.getAddress().setAddressLine1(customerRequest.getAddressLine1());
        customerToUpdate.getAddress().setAddressLine2(customerRequest.getAddressLine2());
        customerToUpdate.getAddress().setZipcode(customerRequest.getZipcode());
        customerToUpdate.getAddress().setCity(foundCity);

        if (isNotPreciseLocation(customerRequest)) {
            try {
                Double[] geolocation = getGeolocation(customerRequest, foundCity.getName());
                customerToUpdate.getAddress().setLatitude(geolocation[0]);
                customerToUpdate.getAddress().setLongitude(geolocation[1]);
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

    private Double[] getGeolocation(CustomerRequest customerRequest, String city) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException, LocationNotFoundException {
        try (GeoApiContext context = new GeoApiContext.Builder().apiKey(GEOLOCATION_KEY).build()) {
            GeocodingResult[] geocodingResults = GeocodingApi
                    .geocode(context,
                            customerRequest.getAddressLine1() + " " +
                                    customerRequest.getAddressLine2() + ", " +
                                    city + " " +
                                    customerRequest.getZipcode())
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
