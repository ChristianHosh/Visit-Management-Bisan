package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.LocationNotFoundException;
import com.example.vm.dto.post.AddressPostDTO;
import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.dto.put.CustomerPutDTO;
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
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
        City city = cityRepository.findById(customerRequest.cityId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CITY_NOT_FOUND));

        Customer customerToSave = Customer.builder()
                .name(customerRequest.name())
                .visitAssignments(new ArrayList<>())
                .enabled(1)
                .address(Address.builder()
                        .addressLine1(customerRequest.addressLine1())
                        .build())
                .build();
        //TODO REMOVE THIS VALIDATION
//        if (!customerRepository.findCustomerByAddress_CityAndName(city, customerRequest.getName()).isEmpty())
//            throw new EntityNotFoundException(EntityNotFoundException.CUSTOMER_ALREADY_EXIST);


//        Customer customerToSave = Customer.builder()
//                .name(customerRequest.getName())
//                .visitAssignments(new ArrayList<>())
//                .enabled(1)
//                .address(Address.builder()
//                        .addressLine1(addressRequest.getAddressLine1())
//                        .addressLine2(addressRequest.getAddressLine2())
//                        .city(city)
//                        .zipcode(addressRequest.getZipcode())
//                        .isPrecise(addressRequest.getPrecise())
//                        .longitude(addressRequest.getPrecise() ? addressRequest.getLongitude() : 0)
//                        .latitude(addressRequest.getPrecise() ? addressRequest.getLatitude() : 0)
//                        .build())
//                .build();
//
//        if (!customerToSave.getAddress().getIsPrecise()) {
//            try {
//                setLngLat(customerToSave, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), city.getName(), addressRequest.getZipcode());
//                customerToSave = customerRepository.save(customerToSave);
//
//                return ResponseEntity.status(HttpStatus.CREATED).body(customerToSave.toDetailPayload());
//            } catch (Exception e) {
//                System.out.println("ERROR " + e.getMessage());
//            }
//
//            throw new LocationNotFoundException();
//        }
//
//        customerToSave = customerRepository.save(customerToSave);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(customerToSave.toDetailPayload());
        return null;
    }

    public ResponseEntity<CustomerDetailPayload> saveContactToCustomer(Long id, ContactPostDTO contactRequest) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getTypes());

        String formattedNumber = PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber());

        contactRequest.setPhoneNumber(formattedNumber);

        Contact newContact = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .email(contactRequest.getEmail())
                .phoneNumber(contactRequest.getPhoneNumber())
                .visitTypes(visitTypes)
                .enabled(1)
                .build();

        foundCustomer.getContacts().add(newContact);
        newContact.setCustomer(foundCustomer);

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(foundCustomer.toDetailPayload());
    }


    //TODO FIX GEOLOCATION PRECISION
    public ResponseEntity<CustomerDetailPayload> updateCustomer(Long id, CustomerPutDTO customerRequest) {
        Customer customerToUpdate = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        AddressPutDTO addressRequest = customerRequest.getAddress();

        City city = cityRepository.findById(addressRequest.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CITY_NOT_FOUND));

        customerToUpdate.setName(customerRequest.getName());

        customerToUpdate.getAddress().setAddressLine1(addressRequest.getAddressLine1());
        customerToUpdate.getAddress().setAddressLine2(addressRequest.getAddressLine2());
        customerToUpdate.getAddress().setCity(city);
        customerToUpdate.getAddress().setZipcode(addressRequest.getZipcode());

        try {
            setLngLat(customerToUpdate, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), city.getName(), addressRequest.getZipcode());

            customerToUpdate = customerRepository.save(customerToUpdate);

            return ResponseEntity.ok(customerToUpdate.toDetailPayload());
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
            throw new LocationNotFoundException();
        }

    }

    public ResponseEntity<CustomerDetailPayload> enableCustomer(Long id) {
        Customer foundCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CUSTOMER_NOT_FOUND));

        foundCustomer.setEnabled(foundCustomer.getEnabled() == 0 ? 1 : 0);

        foundCustomer = customerRepository.save(foundCustomer);

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
    }

    private static void setLngLat(Customer customerToUpdate, String addressLine1, String addressLine2, String
            city, String zipcode) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(GEOLOCATION_KEY)
                .build();

        System.out.println("SEARCHING FOR LOCATION: " + addressLine1 + " " + addressLine2 + ", " +
                city + " " + zipcode);

        GeocodingResult[] results = GeocodingApi.geocode(context,
                addressLine1 + " " + addressLine2 + ", " +
                        city + " " + zipcode).await();

        customerToUpdate.getAddress().setLatitude(results[0].geometry.location.lat);
        customerToUpdate.getAddress().setLongitude(results[0].geometry.location.lng);

        context.shutdown();
    }

}
