package com.example.vm.controller;

import com.example.vm.dto.request.ContactRequest;
import com.example.vm.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getContactById(@PathVariable Long id) {
        return contactService.findContactByUUID(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateContact(@PathVariable Long id,
                                           @RequestBody @Valid ContactRequest contactRequest) {
        return contactService.updateContact(id, contactRequest);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<?> enableCustomer(@PathVariable Long id) {
        return contactService.enableContact(id);
    }

}
