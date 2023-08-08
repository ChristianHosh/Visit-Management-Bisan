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

}
