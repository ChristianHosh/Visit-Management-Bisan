package com.example.vm.controller;

import com.example.vm.dto.put.ContactPutDTO;
import com.example.vm.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getContactById(@PathVariable UUID id) {
        return contactService.findContactByUUID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable UUID id,
                                           @RequestBody @Valid ContactPutDTO contactUpdate) {
        return contactService.updateContact(id, contactUpdate);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableCustomer(@PathVariable UUID id) {
        return contactService.enableContact(id);
    }

}
