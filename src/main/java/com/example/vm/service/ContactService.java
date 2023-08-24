package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.request.ContactRequest;
import com.example.vm.model.Contact;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.ContactRepository;
import com.example.vm.service.formatter.PhoneNumberFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final VisitTypeService visitTypeService;

    public ContactService(ContactRepository contactRepository, VisitTypeService visitTypeService) {
        this.contactRepository = contactRepository;
        this.visitTypeService = visitTypeService;
    }

    public ResponseEntity<Contact> findContactByUUID(Long id) {
        Contact foundContact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CONTACT_NOT_FOUND));

        return ResponseEntity.ok(foundContact);
    }

    public ResponseEntity<Contact> updateContact(Long id, ContactRequest contactRequest) {
        Contact contactToUpdate = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.CONTACT_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getTypes());

        String formattedNumber = PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber());

        contactToUpdate.setFirstName(contactRequest.getFirstName());
        contactToUpdate.setLastName(contactRequest.getLastName());
        contactToUpdate.setPhoneNumber(formattedNumber);
        contactToUpdate.setEmail(contactRequest.getEmail());
        contactToUpdate.setVisitTypes(visitTypes);

        contactToUpdate = contactRepository.save(contactToUpdate);

        return ResponseEntity.ok(contactToUpdate);
    }

    public ResponseEntity<Contact> enableContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        contact.setEnabled(contact.getEnabled() == 0 ? 1 : 0);

        contact = contactRepository.save(contact);

        return ResponseEntity.ok(contact);
    }

}
