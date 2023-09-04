package com.example.vm.repository;

import com.example.vm.model.User;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitAssignmentRepository extends JpaRepository<VisitAssignment, Long> {

    List<VisitAssignment> findByUserAndDateAfter(User user, java.sql.Date date);

    List<VisitAssignment> findByVisitDefinitionAndDateAfter(VisitDefinition visitDefinition, java.sql.Date date);

    Optional<VisitAssignment> findByIdAndEnabledTrue(Long id);

    List<VisitAssignment> findVisitAssignmentsByEnabledTrue();

    List<VisitAssignment> findVisitAssignmentByDateBetween(Date date1, Date date2);

    List<VisitAssignment> findVisitAssignmentByUserAndEnabledTrue(User user);


}
