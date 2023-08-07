package com.example.vm.service;

import com.example.vm.model.Contact;
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

    public Contact saveNewContact(Contact contact) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        contact.setCreatedTime(timestamp);
        contact.setLastModifiedTime(timestamp);

        contact.setFirstName(contact.getFirstName().trim());
        contact.setLastName(contact.getLastName().trim());
        contact.setEmail(contact.getEmail().trim());
        contact.setPhoneNumber(contact.getPhoneNumber().trim());

        return repository.save(contact);
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


    public Contact updateContact(Contact contactToUpdate, Contact updatedContact) {
        contactToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));
        contactToUpdate.setFirstName(updatedContact.getFirstName().trim());
        contactToUpdate.setLastName(updatedContact.getLastName().trim());
        contactToUpdate.setPhoneNumber(updatedContact.getPhoneNumber().trim());
        contactToUpdate.setEmail(updatedContact.getEmail().trim());
        return repository.save(contactToUpdate);
    }


}
