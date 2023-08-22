package com.example.vm.repository;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VisitFormRepository extends JpaRepository<VisitForm, Long> {
 List<VisitForm> findVisitFormByVisitAssignment (VisitAssignment visitAssignment);
 List<VisitForm> findVisitFormByStatus (VisitStatus status);
}
