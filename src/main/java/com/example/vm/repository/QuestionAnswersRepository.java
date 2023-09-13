package com.example.vm.repository;

import com.example.vm.model.templates.QuestionAnswers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionAnswersRepository extends JpaRepository<QuestionAnswers, Long> {
}