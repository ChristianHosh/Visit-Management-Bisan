package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.ContactMapper;
import com.example.vm.dto.request.ContactRequest;
import com.example.vm.dto.response.ContactResponse;
import com.example.vm.model.Contact;
import com.example.vm.model.VisitType;
import com.example.vm.repository.ContactRepository;
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

    public ResponseEntity<ContactResponse> findContactById(Long id) {
        Contact foundContact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CONTACT_NOT_FOUND));

        return ResponseEntity.ok(ContactMapper.toListResponse(foundContact));
    }

    public ResponseEntity<ContactResponse> updateContact(Long id, ContactRequest contactRequest) {
        Contact contactToUpdate = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CONTACT_NOT_FOUND));

        List<VisitType> visitTypes = visitTypeService.getVisitTypes(contactRequest.getVisitTypes());

        ContactMapper.update(contactToUpdate, contactRequest, visitTypes);

        contactToUpdate = contactRepository.save(contactToUpdate);

        return ResponseEntity.ok(ContactMapper.toListResponse(contactToUpdate));
    }

    public ResponseEntity<ContactResponse> enableContact(Long id) {
        Contact foundContact = contactRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        foundContact.setEnabled(!foundContact.getEnabled());

        foundContact = contactRepository.save(foundContact);

        return ResponseEntity.ok(ContactMapper.toListResponse(foundContact));
    }

}
