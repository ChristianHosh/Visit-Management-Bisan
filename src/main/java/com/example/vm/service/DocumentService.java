package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.QuestionAnswersMapper;
import com.example.vm.dto.mapper.ReceiptMapper;
import com.example.vm.dto.mapper.VisitAssignmentMapper;
import com.example.vm.dto.response.QuestionAnswersResponse;
import com.example.vm.dto.response.ReceiptResponse;
import com.example.vm.dto.response.SurveyTemplateResponse;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.templates.QuestionTemplate;
import com.example.vm.repository.PaymentReceiptRepository;
import com.example.vm.repository.QuestionAnswersRepository;
import com.example.vm.repository.QuestionTemplateRepository;
import com.example.vm.repository.VisitAssignmentRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DocumentService {

    private final PaymentReceiptRepository paymentReceiptRepository;
    private final VisitAssignmentRepository visitAssignmentRepository;
    private final QuestionTemplateRepository questionTemplateRepository;
    private final QuestionAnswersRepository questionAnswersRepository;

    public DocumentService(PaymentReceiptRepository paymentReceiptRepository,
                           VisitAssignmentRepository visitAssignmentRepository,
                           QuestionTemplateRepository questionTemplateRepository,
                           QuestionAnswersRepository questionAnswersRepository) {
        this.paymentReceiptRepository = paymentReceiptRepository;
        this.visitAssignmentRepository = visitAssignmentRepository;
        this.questionTemplateRepository = questionTemplateRepository;
        this.questionAnswersRepository = questionAnswersRepository;
    }


    public ResponseEntity<?> getAllReceipts() {
        List<ReceiptResponse> responseList = ReceiptMapper.listToResponse(paymentReceiptRepository.findAll());

        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<?> searchReceipts(Long customerId, String userUsername, String visitTypeName, String startStr, String endStr) {
        Date startDate = startStr == null ? null : Date.valueOf(startStr);
        Date endDate = endStr == null ? null : Date.valueOf(endStr);

        List<ReceiptResponse> responseList = ReceiptMapper
                .listToResponse(paymentReceiptRepository.searchReceipts(customerId, userUsername, visitTypeName, startDate, endDate));

        return ResponseEntity.ok(responseList);
    }

    public ResponseEntity<?> getQuestionsAssignments() {
        List<VisitAssignment> visitAssignmentList = visitAssignmentRepository.findQuestionAssignments();

        return ResponseEntity.ok(VisitAssignmentMapper.listToResponseList(visitAssignmentList));
    }

    public ResponseEntity<?> getQuestionAssignmentAnswers(Long id) {
        VisitAssignment visitAssignment = visitAssignmentRepository.findByIdAndIsQuestionAssignment(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.ASSIGNMENT_NOT_FOUND));

        QuestionTemplate questionTemplate = questionTemplateRepository.findByVisitDefinition(visitAssignment.getVisitDefinition())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.TEMPLATE_NOT_FOUND));

        SurveyTemplateResponse questions = SurveyTemplateResponse.builder()
                .question1(questionTemplate.getQuestion1())
                .question2(questionTemplate.getQuestion2())
                .question3(questionTemplate.getQuestion3())
                .build();

        List<QuestionAnswersResponse> answers = QuestionAnswersMapper.listToResponseList(questionAnswersRepository.findByVisitForm_VisitAssignment(visitAssignment));

        Map<String, Object> response = new HashMap<>();
        response.put("questions", questions);
        response.put("answers", answers);

        return ResponseEntity.ok(response);
    }
}
