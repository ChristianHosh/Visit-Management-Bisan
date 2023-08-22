package com.example.vm.controller;

import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.payload.detail.CustomerDetailPayload;
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

    @GetMapping("")
    public ResponseEntity<?> getAllCustomers() {
        return customerService.findAllCustomers();
    }

    @GetMapping("/assignment")
    public ResponseEntity<?> getAllCustomersWhoHasAssignment() {
        return customerService.findAllCustomersWhoHasAssignment();
    }

    @GetMapping("/enable")
    public ResponseEntity<?> getAllEnabledCustomers() {
        return customerService.findAllEnabledCustomers();
    }


    @GetMapping(value = "/search", params = "query")
    public ResponseEntity<?> searchByQuery(@RequestParam("query") String name) {
        return customerService.searchByQuery(name);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDetailPayload> getCustomerById(@PathVariable Long id) {
        return customerService.findCustomerById(id);
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<?> getContactsByCustomer(@PathVariable Long id) {
        return customerService.getCustomerContacts(id);
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewCustomer(@RequestBody @Valid CustomerPostDTO customerRequest) {
        return customerService.saveNewCustomer(customerRequest);
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<?> SaveContactToCustomer(@PathVariable Long id, @RequestBody @Valid ContactPostDTO contactRequest) {
        return customerService.saveContactToCustomer(id, contactRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody @Valid CustomerPutDTO customerRequest) {
        return customerService.updateCustomer(id, customerRequest);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableCustomer(@PathVariable Long id) {
        return customerService.enableCustomer(id);
    }


}