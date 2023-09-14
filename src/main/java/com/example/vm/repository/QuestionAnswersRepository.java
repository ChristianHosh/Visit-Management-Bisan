package com.example.vm.repository;

import com.example.vm.model.VisitAssignment;
import com.example.vm.model.templates.QuestionAnswers;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionAnswersRepository extends JpaRepository<QuestionAnswers, Long> {
    List<QuestionAnswers> findByVisitForm_VisitAssignment(VisitAssignment visitAssignment);
}