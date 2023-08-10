package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.UUIDDTO;
import com.example.vm.dto.put.ContactPutDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.visit.VisitType;
import com.example.vm.service.ContactService;
import com.example.vm.service.VisitTypeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/contacts")
public class ContactController {
    private final ContactService contactService;
    private final VisitTypeService visitTypeService;

    @Autowired
    public ContactController(ContactService contactService, VisitTypeService visitTypeService) {
        this.contactService = contactService;
        this.visitTypeService = visitTypeService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contact> getContactId(@PathVariable UUID id){
        Contact contact = contactService.findContactByUUID(id);

        if (contact == null)
            throw new UserNotFoundException(UserNotFoundException.CONTACT_NOT_FOUND);

        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "firstName")
    public ResponseEntity<List<Contact>> searchByFirstName(@RequestParam("firstName") String firstName) {
        List<Contact> contactList = contactService.searchContactByFirstName(firstName);
        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<Contact>> searchByLastName(@RequestParam("lastName") String lastName) {
        List<Contact> contactList = contactService.searchContactByLastName(lastName);

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "phoneNumber")
    public ResponseEntity<List<Contact>> searchByPhoneNumber(@RequestParam("phoneNumber") String phoneNumber) {
        List<Contact> contactList = contactService.searchContactByPhoneNumber(phoneNumber);

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "email")
    public ResponseEntity<List<Contact>> searchByEmail(@RequestParam("email") String email) {
        List<Contact> contactList = contactService.searchContactByEmail(email);

        return new ResponseEntity<>(contactList, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contact> updateContact(@PathVariable UUID id,
                                                 @RequestBody @Valid ContactPutDTO contactUpdate) {

        Contact contactToUpdate = contactService.findContactByUUID(id);

        if (contactToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.CONTACT_NOT_FOUND);


        List<VisitType> visitTypes = new ArrayList<>();

        for (UUIDDTO uuiddto : contactUpdate.getTypes()){
            VisitType visitType = visitTypeService.findById(uuiddto.getUuid());

            if (visitType == null)
                throw new UserNotFoundException(UserNotFoundException.TYPE_NOT_FOUND);

            visitTypes.add(visitType);
        }

        Contact updatedContact = contactService.updateContact(contactToUpdate, contactUpdate, visitTypes);

        return new ResponseEntity<>(updatedContact, HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<Contact> enableCustomer(@PathVariable UUID id) {
        Contact contactToEnable = contactService.findContactByUUID(id);

        if (contactToEnable == null)
            throw new UserNotFoundException(UserNotFoundException.CONTACT_NOT_FOUND);

        contactToEnable = contactService.enableContact(contactToEnable);

        return new ResponseEntity<>(contactToEnable, HttpStatus.OK);
    }


}
