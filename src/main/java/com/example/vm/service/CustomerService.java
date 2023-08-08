package com.example.vm.service;

import com.example.vm.dto.post.AddressPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Customer;
import com.example.vm.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

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
        return repository.searchCustomersByName(name);
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
                        .latitude(addressRequest.getLatitude())
                        .longitude(addressRequest.getLongitude())
                        .build())
                .enabled(1)
                .build();

        customerToSave.getAddress().setCreatedTime(timestamp);
        customerToSave.getAddress().setLastModifiedTime(timestamp);

        customerToSave.setCreatedTime(timestamp);
        customerToSave.setLastModifiedTime(timestamp);

        return repository.save(customerToSave);
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
