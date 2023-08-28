package com.example.vm.repository;

import com.example.vm.model.Contact;
import com.example.vm.model.Customer;
import com.example.vm.model.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findContactsByCustomerAndVisitTypesContaining(Customer customer, VisitType visitType);

    int countContactsByVisitTypesContaining(VisitType visitType);
}
