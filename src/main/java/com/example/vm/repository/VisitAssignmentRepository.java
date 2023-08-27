package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitAssignmentRepository extends JpaRepository<VisitAssignment, Long> {
    @Query("SELECT u FROM VisitAssignment u WHERE u.visitDefinition.type =:visit")

    List<VisitAssignment> findVisitAssignmentForSpecificType(VisitType visit);
    List<VisitAssignment> findVisitAssignmentByDate(Date date);

    List<VisitAssignment> findVisitAssignmentsByEnabled(boolean enabled);

    List<VisitAssignment> findVisitAssignmentByDateBetween(Date date1, Date date2);

    List<VisitAssignment> findVisitAssignmentByUserAndEnabled(User user, Boolean enabled);



}
