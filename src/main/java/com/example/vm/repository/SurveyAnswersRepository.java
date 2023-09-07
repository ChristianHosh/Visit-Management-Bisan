package com.example.vm.repository;

import com.example.vm.model.templates.SurveyAnswers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyAnswersRepository extends JpaRepository<SurveyAnswers, Long> {
}