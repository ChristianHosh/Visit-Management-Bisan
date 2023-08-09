package com.example.vm.service;

import com.example.vm.dto.post.AddressPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.AddressPutDTO;
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
            setLngLat(customerToSave, addressRequest.getAddressLine1(), addressRequest.getAddressLine2(), addressRequest.getCity(), addressRequest.getZipcode());

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
        Timestamp timestamp = Timestamp.from(Instant.now());

        AddressPutDTO addressRequest = updatedDTO.getAddress();

        Address oldAddress = customerToUpdate.getAddress();

        customerToUpdate.setName(updatedDTO.getName() == null ? customerToUpdate.getName() : updatedDTO.getName());
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

            return repository.save(customerToUpdate);
        } catch (Exception e) {
            System.out.println("ERROR " + e.getMessage());
        }

        return null;
    }

    private void setLngLat(Customer customerToUpdate, String addressLine1, String addressLine2, String city, String zipcode) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException {
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


    public Customer enableCustomer(Customer customer) {
        customer.setEnabled(customer.getEnabled() == 0 ? 1 : 0);
        return repository.save(customer);
    }


}
