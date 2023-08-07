package com.example.vm.controller;

import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/contacts")
public class ContactController {
        private final ContactService contactService;
        @Autowired
        public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }
    @PutMapping("/{id}")
    public ResponseEntity<Contact> UpdateContactToCustomerByUUID(@PathVariable UUID id,
                                                                 @RequestBody Contact updatedContact) {
        Contact contactToUpdate = contactService.findContactByUUID(id);

        if (contactToUpdate == null)
            throw new UserNotFoundException("UUID NOT FOUND: '" + id + "'");


        validateContact(updatedContact);

        updatedContact = contactService.updateContact(contactToUpdate, updatedContact);

        if (updatedContact == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }
    @PutMapping("/{id}/enable")
    public ResponseEntity<Contact> enableCustomer(@PathVariable UUID id) {
        Contact contactToEnable = contactService.findContactByUUID(id);

        if (contactToEnable == null)
            throw new UserNotFoundException("UUID NOT FOUND : '" );

        contactToEnable  = contactService.enableContact(contactToEnable);

        return new ResponseEntity<>(contactToEnable, HttpStatus.OK);
    }

    @PutMapping("/{id}/disable")
    public ResponseEntity<Contact> disableCustomer(@PathVariable UUID id) {
        Contact contactToDisable = contactService.findContactByUUID(id);

        if (contactToDisable == null)
            throw new UserNotFoundException("UUID NOT FOUND : '" );

        contactToDisable  = contactService.disableContact(contactToDisable);

        return new ResponseEntity<>(contactToDisable, HttpStatus.OK);
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
