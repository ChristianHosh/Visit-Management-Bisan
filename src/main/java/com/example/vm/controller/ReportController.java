package com.example.vm.controller;

import com.example.vm.model.enums.VisitStatus;
import com.example.vm.service.ReportService;
import com.example.vm.service.VisitAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;

@CrossOrigin
@RestController
@RequestMapping("/reports")
public class ReportController {
    private final ReportService reportService;
    private final VisitAssignmentService visitAssignmentService;

    @Autowired
    public ReportController(ReportService reportService, VisitAssignmentService visitAssignmentService) {
        this.reportService = reportService;
        this.visitAssignmentService = visitAssignmentService;
    }

    @GetMapping("/forms/all")
    public ResponseEntity<?> findAllForms() {
        return reportService.getAllForms();

    }

    @GetMapping("/forms/not_started")
    public ResponseEntity<?> findAllNotStartedForms() {
        return reportService.getAllFormsByStatus(VisitStatus.NOT_STARTED);
    }

    @GetMapping("/forms/under_going")
    public ResponseEntity<?> findAllUndergoingForms() {
        return reportService.getAllFormsByStatus(VisitStatus.UNDERGOING);
    }

    @GetMapping("/forms/completed")
    public ResponseEntity<?> findAllCompletedForms() {
        return reportService.getAllFormsByStatus(VisitStatus.COMPLETED);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> findAllCustomerById(@PathVariable Long id) {
        return reportService.findUserAssignmentByCustomer(id);
    }

    @GetMapping("/customers/countByType")
    public ResponseEntity<?> getTypesPercentages() {
        return reportService.getTypesPercentages();
    }


    @GetMapping("/customers/countByArea")
    public ResponseEntity<?> countCustomersInAnArea() {
        return reportService.getCityCustomersPercentage();
    }

    @GetMapping("/visit_assignments")
    public ResponseEntity<?> searchUsersByDateRange(@RequestParam(name = "from", required = false) String date1,
                                                    @RequestParam(name = "to", required = false) String date2) {
        return visitAssignmentService.reportAssignmentByDate(Date.valueOf(date1), Date.valueOf(date2));
    }
    @GetMapping("/users/{username}")
    public ResponseEntity<?> AverageTimeForAUsers(@PathVariable String username) {
        return reportService.findAverageForAUser(username);
    }
}
