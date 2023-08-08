package com.example.vm.service;

import com.example.vm.dto.post.AddressPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Customer;
import com.example.vm.repository.CustomerRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private static final String GEOLOCATION_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

    private final CustomerRepository repository;

    @Autowired
    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public List<Customer> findAllCustomers() {

        return repository.findAll();
    }

    public Customer findCustomerByUUID(UUID uuid) {
        Optional<Customer> customerOptional = repository.findById(uuid);

        return customerOptional.orElse(null);
    }

    public List<Customer> searchByName(String name) {
        return repository.searchCustomersByNameContaining(name);
    }

    public List<Customer> searchByAddressCity(String city) {
        return repository.searchCustomersByAddress_CityContaining(city);
    }

    public List<Customer> searchByAddressLine(String addressLine) {
        return repository.searchCustomersByAddress_AddressLine1ContainingOrAddress_AddressLine2Containing(addressLine, addressLine);
    }

    public Customer saveNewCustomer(CustomerPostDTO customerRequest) {
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
            GeoApiContext context = new GeoApiContext.Builder()
                    .apiKey(GEOLOCATION_KEY)
                    .build();

            System.out.println("SEARCHING FOR LOCATION: " + addressRequest.getAddressLine1() + " " + addressRequest.getAddressLine2() + ", " +
                    addressRequest.getCity() + " " + addressRequest.getZipcode());

            GeocodingResult[] results = GeocodingApi.geocode(context,
                    addressRequest.getAddressLine1() + " " + addressRequest.getAddressLine2() + ", " +
                            addressRequest.getCity() + " " + addressRequest.getZipcode()).await();

            System.out.println("FOUND LOCATIONS: " + results.length);

            customerToSave.getAddress().setLatitude(results[0].geometry.location.lat);
            customerToSave.getAddress().setLongitude(results[0].geometry.location.lng);

            context.shutdown();

            customerToSave.getAddress().setCreatedTime(timestamp);
            customerToSave.getAddress().setLastModifiedTime(timestamp);

            customerToSave.setCreatedTime(timestamp);
            customerToSave.setLastModifiedTime(timestamp);

            return repository.save(customerToSave);

        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }

        return null;
    }

    public Customer updateCustomer(Customer customerToUpdate, CustomerPutDTO updatedDTO) {
        customerToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));
        customerToUpdate.setName(updatedDTO.getName() == null ? customerToUpdate.getName() : updatedDTO.getName());
        return repository.save(customerToUpdate);
    }


    public Customer enableCustomer(Customer customer) {
        customer.setEnabled(customer.getEnabled() == 0 ? 1 : 0);
        return repository.save(customer);
    }


}
