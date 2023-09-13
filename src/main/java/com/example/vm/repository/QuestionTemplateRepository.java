package com.example.vm.repository;

import com.example.vm.model.VisitDefinition;
import com.example.vm.model.templates.QuestionTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuestionTemplateRepository extends JpaRepository<QuestionTemplate, Long> {
    Optional<QuestionTemplate> findByVisitDefinition(VisitDefinition visitDefinition);
}