package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.LocationNotFoundException;
import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.request.CustomerRequest;
import com.example.vm.model.Address;
import com.example.vm.model.City;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.CustomerDetailPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.CustomerListPayload;
import com.example.vm.repository.CityRepository;
import com.example.vm.repository.CustomerRepository;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerService {

    private static final String GEOLOCATION_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

    private final CustomerRepository customerRepository;
    private final CityRepository cityRepository;
    private final VisitTypeService visitTypeService;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CityRepository cityRepository, VisitTypeService visitTypeService) {
        this.customerRepository = customerRepository;
        this.cityRepository = cityRepository;
        this.visitTypeService = visitTypeService;
    }

    public ResponseEntity<List<CustomerListPayload>> findAllCustomers() {
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerRepository.findAll()));
    }

    public ResponseEntity<List<CustomerListPayload>> findAllCustomersWhoHasAssignment() {
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerRepository.findAllCustomerWhoHaveAssignments()));
    }

    public ResponseEntity<List<CustomerListPayload>> findAllEnabledCustomers() {
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerRepository.findCustomerByEnabled(1)));
    }

    public ResponseEntity<CustomerDetailPayload> findCustomerById(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
    }

    public ResponseEntity<List<CustomerListPayload>> searchByQuery(String query) {
        List<Customer> customerList = customerRepository.searchCustomersByNameContainingOrAddress_CityContainingOrAddress_AddressLine1ContainingOrAddress_AddressLine2Containing(query, query, query, query);
        return ResponseEntity.ok(CustomerListPayload.toPayload(customerList));
    }

    public ResponseEntity<List<ContactListPayload>> getCustomerContacts(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        List<Contact> contactList = foundCustomer.getContacts();

        return ResponseEntity.ok(ContactListPayload.toPayload(contactList));
    }

    public ResponseEntity<CustomerDetailPayload> saveNewCustomer(CustomerRequest customerRequest) {
        City foundCity = cityRepository.findById(customerRequest.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CITY_NOT_FOUND));

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

        return ResponseEntity.status(HttpStatus.CREATED).body(customerRepository.save(customerToSave).toDetailPayload());
    }

    public ResponseEntity<CustomerDetailPayload> saveContactToCustomer(Long id, ContactRequest contactRequest) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getTypes());

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

        return ResponseEntity.status(HttpStatus.CREATED).body(foundCustomer.toDetailPayload());
    }

    public ResponseEntity<CustomerDetailPayload> updateCustomer(Long id, CustomerRequest customerRequest) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        City foundCity = cityRepository.findById(customerRequest.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CITY_NOT_FOUND));

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

        return ResponseEntity.ok(customerRepository.save(customerToUpdate).toDetailPayload());
    }

    public ResponseEntity<CustomerDetailPayload> enableCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        foundCustomer.setEnabled(!foundCustomer.getEnabled());

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
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
