package com.example.vm.service;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.UUIDDTO;
import com.example.vm.dto.put.ContactPutDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.ContactRepository;
import com.example.vm.repository.VisitTypeRepository;
import com.example.vm.service.formatter.PhoneNumberFormatter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final VisitTypeRepository visitTypeRepository;

    public ContactService(ContactRepository contactRepository, VisitTypeRepository visitTypeRepository) {
        this.contactRepository = contactRepository;
        this.visitTypeRepository = visitTypeRepository;
    }

    public ResponseEntity<Contact> findContactByUUID(UUID id) {
        Contact foundContact = contactRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.CONTACT_NOT_FOUND));

        return ResponseEntity.ok(foundContact);
    }

    public ResponseEntity<Contact> updateContact(UUID id, ContactPutDTO contactRequest) {
        Contact contactToUpdate = contactRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.CONTACT_NOT_FOUND));

        List<VisitType> visitTypes = new ArrayList<>();

        for (UUIDDTO uuiddto : contactRequest.getTypes()) {
            VisitType visitType = visitTypeRepository.findById(uuiddto.getUuid())
                    .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.TYPE_NOT_FOUND));

            visitTypes.add(visitType);
        }

        String formattedNumber = PhoneNumberFormatter.formatPhone(contactRequest.getPhoneNumber());

        contactRequest.setPhoneNumber(formattedNumber);

        contactToUpdate.setFirstName(contactRequest.getFirstName());
        contactToUpdate.setLastName(contactRequest.getLastName());
        contactToUpdate.setPhoneNumber(contactRequest.getPhoneNumber());
        contactToUpdate.setEmail(contactRequest.getEmail());
        contactToUpdate.setVisitTypes(visitTypes);

        contactToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        contactToUpdate = contactRepository.save(contactToUpdate);

        return ResponseEntity.ok(contactToUpdate);
    }

    public ResponseEntity<Contact> enableContact(UUID id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        contact.setEnabled(contact.getEnabled() == 0 ? 1 : 0);

        contact = contactRepository.save(contact);

        return ResponseEntity.ok(contact);
    }

}
