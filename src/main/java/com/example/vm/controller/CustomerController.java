package com.example.vm.controller;

import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customerList = customerService.findAllCustomers();

        return new ResponseEntity<>(customerList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable UUID id) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Customer> saveNewCustomer(@RequestBody Customer customerToSave) {

        // TODO ADD VALIDATION TO FIELDS

        Customer savedCustomer = customerService.saveNewCustomer(customerToSave);

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }


}
