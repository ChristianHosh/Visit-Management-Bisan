package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.put.ContactPutDTO;
import com.example.vm.model.Contact;
import com.example.vm.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
                                                                 @RequestBody @Valid ContactPutDTO contactUpdate) {

        Contact contactToUpdate = contactService.findContactByUUID(id);

        if (contactToUpdate == null)
            throw new UserNotFoundException("CONTACT NOT FOUND: '" + id + "'");

        Contact updatedContact = contactService.updateContact(contactToUpdate, contactUpdate);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }
    @PutMapping("/{id}/endis")
    public ResponseEntity<Contact> enableCustomer(@PathVariable UUID id) {
        Contact contactToEnable = contactService.findContactByUUID(id);
        if (contactToEnable == null)
            throw new UserNotFoundException("UUID NOT FOUND : '" );
        contactToEnable  = contactService.enableContact(contactToEnable);
        return new ResponseEntity<>(contactToEnable, HttpStatus.OK);
    }
    @GetMapping(value = "/search", params = "firstName")
    public ResponseEntity<List<Contact>> searchByFirstName(@RequestParam("firstName") String firstName) {
        List<Contact> ContactListbyfirstname = contactService.searchContactByFirstName(firstName);
        return new ResponseEntity<>(ContactListbyfirstname, HttpStatus.OK);
    }
    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<Contact>> searchByLastName(@RequestParam("lastName") String lastName) {
        List<Contact> ContactListbylastname = contactService.searchContactByLastName(lastName);

        return new ResponseEntity<>(ContactListbylastname, HttpStatus.OK);
    }
    @GetMapping(value = "/search", params = "PhoneNumber")
    public ResponseEntity<List<Contact>> searchByPhoneNumber(@RequestParam("PhoneNumber") String phoneNumber) {
        List<Contact> ContactListbyphonenumber = contactService.searchContactByLastName(phoneNumber);

        return new ResponseEntity<>(ContactListbyphonenumber, HttpStatus.OK);
    }
    @GetMapping(value = "/search", params = "email")
    public ResponseEntity<List<Contact>> searchByEmail(@RequestParam("email") String email) {
        List<Contact> ContactListbyemail = contactService.searchContactByLastName(email);

        return new ResponseEntity<>(ContactListbyemail, HttpStatus.OK);
    }







}
