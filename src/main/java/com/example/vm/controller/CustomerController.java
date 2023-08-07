package com.example.vm.controller;

import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserNotFoundException;
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

        validateCustomer(customerToSave);
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
            throw new UserNotFoundException("CUSTOMER NOT FOUND WITH ID: '" + id + "'");

        validateContact(contactToSave);
        // THROWS AN EXCEPTION IF VALIDATION FAILS

        contactToSave.setCustomer(customer);

        Contact savedContact = contactService.saveNewContact(contactToSave);

        return new ResponseEntity<>(savedContact, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable UUID id, @RequestBody Customer updatedCustomer) {
        Customer customerToUpdate = customerService.findCustomerByUUID(id);
        if (customerToUpdate == null)
            throw new UserNotFoundException("UUID NOT FOUND : '");

        validateCustomer(updatedCustomer);
        // THROWS AN EXCEPTION IF VALIDATION FAILED

        updatedCustomer = customerService.updateCustomer(customerToUpdate, updatedCustomer);

        if (updatedCustomer == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
    }


    // MOVE TO CONTACT CONTROLLER
    @PutMapping("/{id}/contacts/{contact_id}")
    public ResponseEntity<Contact> UpdateContactToCustomerByUUID(@PathVariable UUID id,
                                                                 @PathVariable(name = "contact_id") UUID contactID,
                                                                 @RequestBody Contact updatedContact) {
        Customer customer = customerService.findCustomerByUUID(id);

        if (customer == null)
            throw new UserNotFoundException("CUSTOMER NOT FOUND WITH ID: '" + id + "'");

        Contact contactToUpdate = contactService.findContactByUUID(contactID);

        if (contactToUpdate == null)
            throw new UserNotFoundException("CONTACT NOT FOUND WITH ID: '" + id + "'");

        validateContact(updatedContact);

        updatedContact = contactService.updateContact(contactToUpdate, updatedContact);

        if (updatedContact == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }
    // DISABLE CONTACT IN CONTACT CONTROLLER
    // ENABLE CONTACT IN CONTACT CONTROLLER

    @PutMapping("/{id}/enable")
    public ResponseEntity<Customer> enableCustomer(@PathVariable UUID id) {
        Customer customerToEnable = customerService.findCustomerByUUID(id);

        if (customerToEnable == null)
            throw new UserNotFoundException("UUID NOT FOUND : '" );

        customerToEnable  = customerService.enableCustomer(customerToEnable);

        return new ResponseEntity<>(customerToEnable, HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Customer> disableCustomer(@PathVariable UUID id) {
        Customer customerToDisable = customerService.findCustomerByUUID(id);

        if (customerToDisable == null)
            throw new UserNotFoundException("UUID NOT FOUND : '" );

        customerToDisable  = customerService.disableCustomer(customerToDisable);

        return new ResponseEntity<>(customerToDisable, HttpStatus.OK);
    }


    private void validateCustomer(Customer customer) {
        if (Customer.isNotValidLength(customer.getName().trim()))
            throw new InvalidUserArgumentException("LENGTH IS NOT VALID, SHOULD BE lESS THAN 30");

    }

    private void validateContact(Contact contact) {
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