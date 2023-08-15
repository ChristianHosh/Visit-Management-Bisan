package com.example.vm.repository;

import com.example.vm.model.visit.VisitForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VisitFormRepository extends JpaRepository<VisitForm, UUID> {

}
