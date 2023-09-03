package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitForm;
import com.example.vm.model.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitFormRepository extends JpaRepository<VisitForm, Long> {
    Optional<VisitForm> findByCustomerAndVisitAssignment(Customer customer, VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByVisitAssignment(VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByVisitAssignmentAndEnabledTrue(VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByStatus(VisitStatus status);

    List<VisitForm> findVisitFormsByEnabled(Boolean enabled);

}
