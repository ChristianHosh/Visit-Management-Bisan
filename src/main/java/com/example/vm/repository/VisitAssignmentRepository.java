package com.example.vm.repository;

import com.example.vm.model.User;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitDefinition;
import com.example.vm.model.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitAssignmentRepository extends JpaRepository<VisitAssignment, Long> {
    List<VisitAssignment> findByVisitDefinitionAndDateAfter(VisitDefinition visitDefinition, java.sql.Date date);
    Optional<VisitAssignment> findByIdAndEnabledTrue(Long id);
    @Query("SELECT u FROM VisitAssignment u WHERE u.visitDefinition.type =:visit")

    List<VisitAssignment> findVisitAssignmentForSpecificType(VisitType visit);

    List<VisitAssignment> findVisitAssignmentsByEnabledTrue();

    List<VisitAssignment> findVisitAssignmentByDateBetween(Date date1, Date date2);

    List<VisitAssignment> findVisitAssignmentByUserAndEnabledTrue(User user);



}
