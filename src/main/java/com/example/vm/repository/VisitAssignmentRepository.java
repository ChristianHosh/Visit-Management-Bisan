package com.example.vm.repository;

import com.example.vm.model.visit.VisitAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface VisitAssignmentRepository extends JpaRepository<VisitAssignment, UUID> {

}
