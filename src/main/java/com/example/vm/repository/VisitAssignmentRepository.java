package com.example.vm.repository;

import com.example.vm.model.visit.VisitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface VisitAssignmentRepository extends JpaRepository<VisitAssignment, Long> {
    List<VisitAssignment> findVisitAssignmentByDate(Date date);

    List<VisitAssignment> findVisitAssignmentByDateAfterAndDateBefore(Date date1, Date date2);

}
