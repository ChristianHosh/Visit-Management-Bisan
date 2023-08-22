package com.example.vm.controller;

import com.example.vm.service.CustomerService;
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
    private final ReportService reportservice;
    private final VisitAssignmentService visitAssignmentService;
    private final CustomerService customerService;

    @Autowired
    public ReportController(ReportService reportservice, VisitAssignmentService visitAssignmentService, CustomerService customerService) {
        this.reportservice = reportservice;

        this.visitAssignmentService = visitAssignmentService;
        this.customerService = customerService;
    }

    @GetMapping("/forms/all")
    public ResponseEntity<?> findAllForms() {
        return reportservice.reportForAllForms();

    }

    @GetMapping("/forms/not_started")
    public ResponseEntity<?> findAllNotStartedStatus() {
        return reportservice.reportForNotStartedStatus();
    }

    @GetMapping("/forms/under_going")
    public ResponseEntity<?> findAllUndergoingStatus() {
        return reportservice.reportForUnderGoingStatus();
    }

    @GetMapping("/forms/completed")
    public ResponseEntity<?> findAllCompleted() {
        return reportservice.reportForCompleted();
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> findAllCustomerById(@PathVariable Long id) {
        return customerService.findCustomer(id);
    }

    @GetMapping("/customers/countByType")
    public ResponseEntity<?> countAllCustomerForaType() {
        return customerService.countAllCustomer();
    }

    @GetMapping("/customers/countByArea")
    public ResponseEntity<?> countCustomersInAnArea() {
        return customerService.CustomersInSpecificArea();
    }

    @GetMapping("/visit_assignments")
    public ResponseEntity<?> searchUsersByDateRange(@RequestParam(name = "from", required = false) String date1,
                                                    @RequestParam(name = "to", required = false) String date2) {
        return visitAssignmentService.reportAssignmentByDate(Date.valueOf(date1), Date.valueOf(date2));
    }
}