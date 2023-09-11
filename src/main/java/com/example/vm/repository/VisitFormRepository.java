package com.example.vm.repository;

import com.example.vm.model.*;
import com.example.vm.model.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitFormRepository extends JpaRepository<VisitForm, Long> {
    List<VisitForm> findByCustomerAndVisitAssignment_DateBetweenAndEnabledTrueAndVisitAssignment_EnabledTrueAndStartTimeNotNull(Customer customer, Date dateStart, Date dateEnd);

    List<VisitForm> findByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(Customer customer, Date dateStart, Date dateEnd, VisitStatus status);

    long countByCustomerAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(Customer customer, Date dateStart, Date dateEnd, VisitStatus status);

    long countByCustomerAndVisitAssignment_DateBetweenAndCustomer_EnabledTrueAndEnabledTrue(Customer customer, Date dateStart, Date dateEnd);

    List<VisitForm> findByVisitAssignment_UserAndVisitAssignment_DateBetweenAndVisitAssignment_EnabledTrueAndEnabledTrue(User user, Date dateStart, Date dateEnd);

    long countByVisitAssignment_VisitDefinition_LocationAndVisitAssignment_DateBetweenAndVisitAssignment_EnabledTrueAndEnabledTrue(Location location, Date dateStart, Date dateEnd);

    long countByVisitAssignmentAndStartTimeAfterAndEnabledTrue(VisitAssignment visitAssignment, Timestamp startTime);

    List<VisitForm> findByVisitAssignment_UserAndVisitAssignment_DateBetweenAndVisitAssignment_EnabledTrueAndEnabledTrueAndStatus(User user, Date dateStart, Date dateEnd, VisitStatus status);

    long countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndStatusAndEnabledTrueAndVisitAssignment_EnabledTrue(User user, Date dateStart, Date dateEnd, VisitStatus status);

    long countByVisitAssignment_UserAndVisitAssignment_DateBetweenAndEnabledTrueAndVisitAssignment_EnabledTrue(User user, Date dateStart, Date dateEnd);

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
