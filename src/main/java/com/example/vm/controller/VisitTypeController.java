package com.example.vm.controller;

import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.VisitTypePostDTO;
import com.example.vm.dto.put.UserPutDTO;
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
    public ResponseEntity<?> getAllVisitTypes() {
     return visitTypeService.findAll();
    }
    @PostMapping("")
    public ResponseEntity<?> saveNewVisitType(@RequestBody @Valid VisitTypePostDTO visitTypeRequest) {
       return visitTypeService.saveNewVisitType(visitTypeRequest);

    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisitType(@PathVariable UUID id, @RequestBody @Valid VisitTypePutDTO visitTypeUpdate) {
        return  visitTypeService.updateVisitType(id,visitTypeUpdate);
    }

}
