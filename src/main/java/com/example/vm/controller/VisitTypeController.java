package com.example.vm.controller;

import com.example.vm.dto.post.VisitTypePostDTO;
import com.example.vm.model.visit.VisitType;
import com.example.vm.service.VisitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


}
