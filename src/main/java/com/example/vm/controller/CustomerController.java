package com.example.vm.controller;

import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.service.ContactService;
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
    private final ContactService contactService;

    @Autowired
    public CustomerController(CustomerService customerService, ContactService contactService) {
        this.customerService = customerService;
        this.contactService = contactService;
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

        ValidateCustomer(customerToSave);
        // THROWS AN EXCEPTION IF VALIDATION FAILS

        Customer savedCustomer = customerService.saveNewCustomer(customerToSave);

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
    public ResponseEntity<Contact> SaveContactToCustomerByUUID(@PathVariable UUID id, @RequestBody Contact contactToSave) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        ValidateContact(contactToSave);
        // THROWS AN EXCEPTION IF VALIDATION FAILS

        contactToSave.setCustomer(customer);

        Contact savedContact = contactService.saveNewContact(contactToSave);

        return new ResponseEntity<>(savedContact, HttpStatus.OK);
    }

    private void ValidateCustomer(Customer customer) {
        if (Customer.isNotValidLength(customer.getName().trim()))
            throw new InvalidUserArgumentException("LENGTH IS NOT VALID, SHOULD BE lESS THAN 30");

    }

    private void ValidateContact(Contact contact) {
        if (Contact.isNotValidName(contact.getFirstName().trim()))
            throw new InvalidUserArgumentException("FIRST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        if (Contact.isNotValidName(contact.getLastName()))
            throw new InvalidUserArgumentException("LAST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        if (Contact.isNotValidLength(contact.getFirstName()) || Contact.isNotValidLength(contact.getLastName()))
            throw new InvalidUserArgumentException("LENGTH IS NOT VALID, SHOULD BE lESS THAN 30");

        if (Contact.isNotValidEmail(contact.getEmail().trim()))
            throw new InvalidUserArgumentException("EMAIL IS NOT VALID, CHECK AGAIN");

        if (Contact.isNotValidNumber(contact.getPhoneNumber().trim()))
            throw new InvalidUserArgumentException("PHONE NUMBER IS NOT VALID, CHECK AGAIN");


    }
}