package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.model.Contact;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.service.VisitAssignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/assignments")
public class VisitAssignmentController {
    private final VisitAssignmentService visitAssignmentService;

    public VisitAssignmentController(VisitAssignmentService visitAssignmentService) {
        this.visitAssignmentService = visitAssignmentService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<VisitAssignment> getVisitAssignmentById(@PathVariable UUID id){
        VisitAssignment  VisitAssignment = visitAssignmentService.findVisitAssignmentByUUID(id);
        return new ResponseEntity<>(VisitAssignment, HttpStatus.OK);
    }


}
