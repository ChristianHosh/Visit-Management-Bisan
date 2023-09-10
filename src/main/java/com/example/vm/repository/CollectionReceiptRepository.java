package com.example.vm.repository;

import com.example.vm.model.templates.CollectionReceipt;
import com.example.vm.model.templates.SurveyAnswers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CollectionReceiptRepository  extends JpaRepository<CollectionReceipt, Long> {
}
