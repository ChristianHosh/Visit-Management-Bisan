package com.example.vm.service;

import com.example.vm.dto.post.ContactPostDTO;
import com.example.vm.dto.put.ContactPutDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.ContactRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ContactService {
    private final ContactRepository repository;

    public ContactService(ContactRepository repository) {
        this.repository = repository;
    }

    public Contact findContactByUUID(UUID uuid) {
        Optional<Contact> optional = repository.findById(uuid);
        return optional.orElse(null);
    }

    public Contact saveNewContact(Customer customer, ContactPostDTO contactRequest, List<VisitType> visitTypes) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        Contact contactToSave = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .phoneNumber(contactRequest.getPhoneNumber())
                .email(contactRequest.getEmail())
                .enabled(1)
                .build();

        contactToSave.setCreatedTime(timestamp);
        contactToSave.setLastModifiedTime(timestamp);

        contactToSave.setVisitTypes(visitTypes);
        contactToSave.setCustomer(customer);

        return repository.save(contactToSave);
    }

    public List<Contact> searchContactByFirstName(String firstName) {
        return repository.searchContactByFirstNameContaining(firstName);
    }

    public List<Contact> searchContactByLastName(String lastName) {
        return repository.searchContactByLastNameContaining(lastName);
    }

    public List<Contact> searchContactByPhoneNumber(String phoneNumber) {
        return repository.searchContactByPhoneNumberContaining(phoneNumber);
    }

    public List<Contact> searchContactByEmail(String email) {
        return repository.searchContactByEmailContaining(email);
    }


    public Contact updateContact(Contact contactToUpdate, ContactPutDTO updatedDTO, List<VisitType> visitTypes) {

        contactToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        contactToUpdate.setFirstName(updatedDTO.getFirstName() == null ? contactToUpdate.getFirstName() : updatedDTO.getFirstName());
        contactToUpdate.setLastName(updatedDTO.getLastName() == null ? contactToUpdate.getLastName() : updatedDTO.getLastName());
        contactToUpdate.setPhoneNumber(updatedDTO.getPhoneNumber() == null ? contactToUpdate.getPhoneNumber() : updatedDTO.getPhoneNumber());
        contactToUpdate.setEmail(updatedDTO.getEmail() == null ? contactToUpdate.getEmail() : updatedDTO.getEmail());

        contactToUpdate.setVisitTypes(visitTypes);

        return repository.save(contactToUpdate);
    }

    public Contact enableContact(Contact contact) {
        contact.setEnabled(contact.getEnabled() == 0 ? 1 : 0);

        return repository.save(contact);
    }

    public List<Contact> findContactsByCustomerAndVisitTypes(Customer customer, VisitType visitType){
        return repository.findContactsByCustomerAndVisitTypesContaining(customer, visitType);
    }


}
