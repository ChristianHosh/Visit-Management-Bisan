package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.service.VisitAssignmentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/assignments")
public class VisitAssignmentController {
    private final VisitAssignmentService visitAssignmentService;

    public VisitAssignmentController(VisitAssignmentService visitAssignmentService) {
        this.visitAssignmentService = visitAssignmentService;
    }

    @GetMapping("")
    public ResponseEntity<List<VisitAssignment>> getAllVisitAssignment(){
        List<VisitAssignment> visitAssignmentList = visitAssignmentService.findAllVisitAssignments();

        return new ResponseEntity<>(visitAssignmentList, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<VisitAssignment> getVisitAssignmentById(@PathVariable UUID id){
        VisitAssignment  VisitAssignment = visitAssignmentService.findVisitAssignmentByUUID(id);
        return new ResponseEntity<>(VisitAssignment, HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<VisitAssignment> updateContact(@PathVariable UUID id,
                                                 @RequestBody @Valid VisitAssignmentPutDTO visitAssignmentUpdate) {

        VisitAssignment visitAssignmentToUpdate = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignmentToUpdate == null)
            throw new UserNotFoundException();

        VisitAssignment updatedVisitAssignment = visitAssignmentService.updateVisitAssignment(visitAssignmentToUpdate,visitAssignmentUpdate);

        return new ResponseEntity<>(updatedVisitAssignment, HttpStatus.OK);
    }

    @PutMapping("/{id}/endis")
    public ResponseEntity<VisitAssignment> enableCustomer(@PathVariable UUID id) {
        VisitAssignment visitAssignmentToEnable = visitAssignmentService.findVisitAssignmentByUUID(id);

        if (visitAssignmentToEnable == null)
            throw new UserNotFoundException();

        visitAssignmentToEnable = visitAssignmentService.enableVisitAssignment(visitAssignmentToEnable);

        return new ResponseEntity<>(visitAssignmentToEnable, HttpStatus.OK);
    }




}
