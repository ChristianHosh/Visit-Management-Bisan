package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.post.CustomerPostDTO;
import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.dto.put.CustomerPutDTO;
import com.example.vm.model.Address;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.service.AddressService;
import com.example.vm.service.ContactService;
import com.example.vm.service.CustomerService;
import jakarta.validation.Valid;
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
    private final ContactService contactService;

    private final AddressService addressService;

    @Autowired
    public CustomerController(CustomerService customerService, ContactService contactService, AddressService addressService) {
        this.customerService = customerService;
        this.contactService = contactService;
        this.addressService = addressService;
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
    public ResponseEntity<Customer> saveNewCustomer(@RequestBody @Valid CustomerPostDTO customerRequest) {
        Customer savedCustomer = customerService.saveNewCustomer(customerRequest);

        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/contacts")
    public ResponseEntity<List<Contact>> getContactsByCustomerUUID(@PathVariable UUID id) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        List<Contact> contactList = customer.getContacts();

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @PostMapping("/{id}/contacts")
    public ResponseEntity<Contact> SaveContactToCustomerByUUID(@PathVariable UUID id, @RequestBody @Valid ContactPostDTO contactRequest) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            throw new UserNotFoundException("CUSTOMER NOT FOUND WITH ID: '" + id + "'");


        Contact savedContact = contactService.saveNewContact(customer, contactRequest);

        return new ResponseEntity<>(savedContact, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @RequestBody @Valid CustomerPutDTO customerupdated) {
        Customer customerToUpdate = customerService.findCustomerByUUID(id);
        if (customerToUpdate == null)
            throw new UserNotFoundException("UUID NOT FOUND : '");

      Customer updatedCustomer = customerService.updateCustomer(customerToUpdate,customerupdated);
      return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }

    @PutMapping("/{id}/address")
    public ResponseEntity<Address> updateCustomerAddress (@PathVariable UUID id, @RequestBody @Valid AddressPutDTO updatedAddress ) {

        Customer customerToUpdate = customerService.findCustomerByUUID(id);
        if (customerToUpdate == null)
            throw new UserNotFoundException("UUID NOT FOUND : '");

        Address updatedCustomer = addressService.updateCustomerAddress(customerToUpdate.getAddress(), updatedAddress);

        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }







    @PutMapping("/{id}/endis")
    public ResponseEntity<Customer> enableCustomer(@PathVariable UUID id) {
        Customer customerToEnable = customerService.findCustomerByUUID(id);

        if (customerToEnable == null)
            throw new UserNotFoundException("UUID NOT FOUND : '");

        customerToEnable = customerService.enableCustomer(customerToEnable);

        return new ResponseEntity<>(customerToEnable, HttpStatus.OK);
    }
}