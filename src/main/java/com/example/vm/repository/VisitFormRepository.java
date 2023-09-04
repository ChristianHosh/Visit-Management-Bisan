package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.Location;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitForm;
import com.example.vm.model.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitFormRepository extends JpaRepository<VisitForm, Long> {
    long countByVisitAssignment_VisitDefinition_Location(Location location);
    long countByStatus(VisitStatus status);
    long countByVisitAssignment_DateBetweenAndStatus(Date dateStart, Date dateEnd, VisitStatus status);
    long countByVisitAssignment_DateAndStatus(Date date, VisitStatus status);
    long countByVisitAssignment_Date(Date date);
    Optional<VisitForm> findByCustomerAndVisitAssignment(Customer customer, VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByVisitAssignment(VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByVisitAssignmentAndEnabledTrue(VisitAssignment visitAssignment);

    List<VisitForm> findVisitFormByStatus(VisitStatus status);

    List<VisitForm> findVisitFormsByEnabled(Boolean enabled);

}
