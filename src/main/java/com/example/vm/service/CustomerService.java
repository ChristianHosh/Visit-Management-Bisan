package com.example.vm.service;

import com.example.vm.controller.error.exception.LocationNotFoundException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.AddressPostDTO;
import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.payload.detail.CustomerDetailPayload;
import com.example.vm.payload.list.ContactListPayload;
import com.example.vm.payload.list.CustomerListPayload;
import com.example.vm.repository.CustomerRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerService {

    private static final String GEOLOCATION_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<CustomerListPayload>> findAllCustomers() {
        return ResponseEntity.ok(toCustomerPayloadList(repository.findAll()));
    }

    public ResponseEntity<CustomerDetailPayload> findCustomerByUUID(UUID id) {
        Customer foundCustomer = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
    }

    public ResponseEntity<List<CustomerListPayload>> searchByName(String name) {
        List<Customer> customerList = repository.searchCustomersByNameContaining(name);

        return ResponseEntity.ok(toCustomerPayloadList(customerList));
    }

    public ResponseEntity<List<CustomerListPayload>> searchByAddressCity(String city) {
        List<Customer> customerList = repository.searchCustomersByAddress_CityContaining(city);

        return ResponseEntity.ok(toCustomerPayloadList(customerList));
    }

    public ResponseEntity<List<CustomerListPayload>> searchByAddressLine(String addressLine) {
        List<Customer> customerList = repository.searchCustomersByAddress_AddressLine1ContainingOrAddress_AddressLine2Containing(addressLine, addressLine);

        return ResponseEntity.ok(toCustomerPayloadList(customerList));
    }

    public ResponseEntity<List<ContactListPayload>> getCustomerContacts(UUID id) {
        Customer foundCustomer = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND));

        List<Contact> contactList = foundCustomer.getContacts();

        return ResponseEntity.ok(toContactPayloadList(contactList));
    }

    public ResponseEntity<CustomerDetailPayload> saveNewCustomer(CustomerPostDTO customerRequest) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        AddressPostDTO addressRequest = customerRequest.getAddress();

        Customer customerToSave = Customer.builder()
                .name(customerRequest.getName())
                .address(Address.builder()
                        .addressLine1(addressRequest.getAddressLine1())
                        .addressLine2(addressRequest.getAddressLine2())
                        .zipcode(addressRequest.getZipcode())
                        .city(addressRequest.getCity())
                        .build())
                .enabled(1)
                .build();

        try {
            setLngLat(customerToSave, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), addressRequest.getCity(), addressRequest.getZipcode());

            customerToSave.getAddress().setCreatedTime(timestamp);
            customerToSave.getAddress().setLastModifiedTime(timestamp);

            customerToSave.setCreatedTime(timestamp);
            customerToSave.setLastModifiedTime(timestamp);

            customerToSave = repository.save(customerToSave);

            return ResponseEntity.status(HttpStatus.CREATED).body(customerToSave.toDetailPayload());

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }

        throw new LocationNotFoundException();
    }


    public ResponseEntity<CustomerDetailPayload> saveContactToCustomer(UUID id, ContactPostDTO contactRequest) {
        Customer foundCustomer = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND));

        String formattedNumber = contactRequest.getPhoneNumber();

        contactRequest.setPhoneNumber(formattedNumber);

        Contact newContact = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .email(contactRequest.getEmail())
                .phoneNumber(contactRequest.getPhoneNumber())
                .enabled(1)
                .build();

        foundCustomer.getContacts().add(newContact);

        foundCustomer = repository.save(foundCustomer);

        return ResponseEntity.status(HttpStatus.CREATED).body(foundCustomer.toDetailPayload());
    }

    public ResponseEntity<CustomerDetailPayload> updateCustomer(UUID id, CustomerPutDTO customerRequest) {
        Customer customerToUpdate = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND));

        Timestamp timestamp = Timestamp.from(Instant.now());

        AddressPutDTO addressRequest = customerRequest.getAddress();

        Address oldAddress = customerToUpdate.getAddress();

        customerToUpdate.setName(customerRequest.getName());
        customerToUpdate.setAddress(Address.builder()
                .addressLine1(addressRequest.getAddressLine1())
                .addressLine2(addressRequest.getAddressLine2())
                .city(addressRequest.getCity())
                .zipcode(addressRequest.getZipcode())
                .build());

        try {
            setLngLat(customerToUpdate, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), addressRequest.getCity(), addressRequest.getZipcode());

            customerToUpdate.setLastModifiedTime(timestamp);

            customerToUpdate.getAddress().setLastModifiedTime(timestamp);
            customerToUpdate.getAddress().setCreatedTime(oldAddress.getCreatedTime());

            customerToUpdate = repository.save(customerToUpdate);

            return ResponseEntity.ok(customerToUpdate.toDetailPayload());
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }

        throw new LocationNotFoundException();
    }

    public ResponseEntity<CustomerDetailPayload> enableCustomer(UUID id) {
        Customer foundCustomer = repository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.CUSTOMER_NOT_FOUND));

        foundCustomer.setEnabled(foundCustomer.getEnabled() == 0 ? 1 : 0);

        foundCustomer = repository.save(foundCustomer);

        return ResponseEntity.ok(foundCustomer.toDetailPayload());
    }

    private static void setLngLat(Customer customerToUpdate, String addressLine1, String addressLine2, String city, String zipcode) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException {
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


    private static List<CustomerListPayload> toCustomerPayloadList(List<Customer> customerList) {
        return customerList.stream().map(Customer::toListPayload).toList();
    }

    private static List<ContactListPayload> toContactPayloadList(List<Contact> contactList) {
        return contactList.stream().map(Contact::toListPayload).toList();
    }

}
