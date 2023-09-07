package com.example.vm.repository;

import com.example.vm.model.VisitAssignment;
import com.example.vm.model.templates.SurveyTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyTemplateRepository extends JpaRepository<SurveyTemplate, Long> {
    Optional<SurveyTemplate> findByVisitAssignment(VisitAssignment visitAssignment);

}
