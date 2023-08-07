package com.example.vm.service;

import com.example.vm.dto.ContactRequestDTO;
import com.example.vm.dto.ContactUpdateDTO;
import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
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

    public List<Contact> findAllContacts() {
        return repository.findAll();
    }

    public Contact findContactByUUID(UUID uuid) {
        Optional<Contact> optional = repository.findById(uuid);
        return optional.orElse(null);
    }

    public Contact saveNewContact(Customer customer,ContactRequestDTO contactRequest) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        Contact contactToSave = Contact.builder()
                .firstName(contactRequest.getFirstName())
                .lastName(contactRequest.getLastName())
                .phoneNumber(contactRequest.getPhoneNumber())
                .email(contactRequest.getEmail())
                .build();

        contactToSave.setCreatedTime(timestamp);
        contactToSave.setLastModifiedTime(timestamp);

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


    public Contact updateContact(Contact contactToUpdate, ContactUpdateDTO updatedDTO) {

        contactToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        contactToUpdate.setFirstName(updatedDTO.getFirstName() == null ? contactToUpdate.getFirstName() : updatedDTO.getFirstName());
        contactToUpdate.setLastName(updatedDTO.getLastName() == null ? contactToUpdate.getLastName() : updatedDTO.getLastName());
        contactToUpdate.setPhoneNumber(updatedDTO.getPhoneNumber() == null ? contactToUpdate.getPhoneNumber() : updatedDTO.getPhoneNumber());
        contactToUpdate.setEmail(updatedDTO.getEmail() == null ? contactToUpdate.getEmail() : updatedDTO.getEmail());

        return repository.save(contactToUpdate);
    }

    public Contact enableContact(Contact contact) {
        contact.setEnabled(contact.getEnabled() == 0 ? 1 : 0);
        return repository.save(contact);
    }


}
