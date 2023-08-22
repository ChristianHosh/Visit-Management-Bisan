package com.example.vm.service;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.payload.report.FormReportListPayload;
import com.example.vm.repository.VisitFormRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportService {

    private final VisitFormRepository visitFormRepository;

    @Autowired
    public ReportService(VisitFormRepository visitFormRepository) {
        this.visitFormRepository = visitFormRepository;
    }

    public ResponseEntity<List<FormReportListPayload>> getAllForms() {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findAll()));
    }
    public ResponseEntity<List<FormReportListPayload>> getAllFormsByStatus(VisitStatus status) {
        return ResponseEntity.ok(FormReportListPayload.toPayload(visitFormRepository.findVisitFormByStatus(status)));
    }

}
