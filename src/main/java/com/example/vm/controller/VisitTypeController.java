package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitTypePostDTO;
import com.example.vm.dto.put.VisitAssignmentPutDTO;
import com.example.vm.dto.put.VisitTypePutDTO;
import com.example.vm.model.visit.VisitAssignment;
import com.example.vm.model.visit.VisitType;
import com.example.vm.payload.detail.VisitAssignmentDetailPayload;
import com.example.vm.service.VisitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/visit_types")
public class VisitTypeController {
    private final VisitTypeService visitTypeService;


    public VisitTypeController(VisitTypeService visitTypeService) {
        this.visitTypeService = visitTypeService;
    }

    @GetMapping("")
    public ResponseEntity<List<VisitType>> getAllVisitTypes() {
        List<VisitType> visitDefinitionsList = visitTypeService.findAll();
        return new ResponseEntity<>(visitDefinitionsList, HttpStatus.OK);
    }
    @PostMapping("")
    public ResponseEntity<VisitType> saveNewVisitType(@RequestBody @Valid VisitTypePostDTO visitTypeRequest) {
        VisitType visitType = visitTypeService.saveNewVisitType(visitTypeRequest);

        return new ResponseEntity<>(visitType, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<VisitType> updateVisitType(@PathVariable UUID id,
                                                                      @RequestBody @Valid VisitTypePutDTO visitTypeUpdate) {

        VisitType visitTypeToUpdate = visitTypeService.findById(id);

        if (visitTypeToUpdate == null)
            throw new UserNotFoundException(UserNotFoundException.ASSIGNMENT_NOT_FOUND);

        VisitType updatedVisitType =visitTypeService.updateVisitType(visitTypeToUpdate,visitTypeUpdate);

        return new ResponseEntity<>(updatedVisitType, HttpStatus.OK);
    }



}
