package com.example.vm.controller;

import com.example.vm.service.CustomerService;
import com.example.vm.service.ReportService;
import com.example.vm.service.VisitAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.UUID;

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
    public ResponseEntity<?> findAllNotStartedStatus (){
        return reportservice.reportForNotStartedStatus();
    }
    @GetMapping("/forms/under_going")
    public ResponseEntity<?> findAllUndergoingStatus (){
        return reportservice.reportForUnderGoingStatus();
    }
    @GetMapping("/forms/completed")
    public ResponseEntity<?> findAllCompleted (){
        return reportservice.reportForCompleted();
    }
    @GetMapping("/visit_assignments/{date}")
    public ResponseEntity<?> findAllAssignmentByDate (@PathVariable String date){
        return  visitAssignmentService.reportAssignmentByDate(Date.valueOf(date));
    }
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> findAllCustomerById(@PathVariable UUID id){
        return customerService.findCustomer(id);
    }


}
