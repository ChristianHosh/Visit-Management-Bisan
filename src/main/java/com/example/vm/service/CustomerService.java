package com.example.vm.service;

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

    public List<Customer> findAllCustomers(){
        return repository.findAll();
    }

    public Customer findCustomerByUUID(UUID uuid){
        Optional<Customer> customerOptional = repository.findById(uuid);
        return customerOptional.orElse(null);
    }

    public List<Customer> searchByName(String name){
        return repository.searchCustomersByName(name);
    }

    public Customer saveNewCustomer(Customer customerToSave){
        Timestamp timestamp = Timestamp.from(Instant.now());

        customerToSave.setCreatedTime(timestamp);
        customerToSave.setLastModifiedTime(timestamp);

        customerToSave.setName(customerToSave.getName().trim());

        return repository.save(customerToSave);
    }

    public Customer updateCustomer(Customer customerToUpdate, Customer updatedCustomer){
        customerToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        customerToUpdate.setName(updatedCustomer.getName());

        return repository.save(customerToUpdate);
    }
}
