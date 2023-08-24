package com.example.vm.controller;

import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.request.CustomerRequest;
import com.example.vm.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCustomers() {
        return customerService.findAllCustomers();
    }
    @GetMapping("")
    public ResponseEntity<?> getAllEnableCustomers() {
        return customerService.findAllEnabledCustomers();
    }

    @GetMapping("/assignment")
    public ResponseEntity<?> getAllCustomersWhoHasAssignment() {
        return customerService.findAllCustomersWhoHasAssignment();
    }

    @GetMapping(value = "/search", params = "query")
    public ResponseEntity<?> searchByQuery(@RequestParam("query") String name) {
        return customerService.searchByQuery(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<?> getContactsByCustomer(@PathVariable Long id) {
        return customerService.getCustomerContacts(id);
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewCustomer(@RequestBody @Valid CustomerRequest customerRequest) {
        return customerService.saveNewCustomer(customerRequest);
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<?> SaveContactToCustomer(@PathVariable Long id, @RequestBody @Valid ContactRequest contactRequest) {
        return customerService.saveContactToCustomer(id, contactRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerRequest customerRequest) {
        return customerService.updateCustomer(id, customerRequest);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableCustomer(@PathVariable Long id) {
        return customerService.enableCustomer(id);
    }


}