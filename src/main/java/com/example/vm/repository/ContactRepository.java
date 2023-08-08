package com.example.vm.repository;

import com.example.vm.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    List<Contact> searchContactByFirstNameContaining(String firstName);
    List<Contact> searchContactByLastNameContaining(String lastName);
    List<Contact> searchContactByPhoneNumberContaining(String phoneNumber);
    List<Contact> searchContactByEmailContaining(String email);

}
