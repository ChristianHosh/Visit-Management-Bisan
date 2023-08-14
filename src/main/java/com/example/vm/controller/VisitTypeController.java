package com.example.vm.controller;

import com.example.vm.dto.post.VisitTypePostDTO;
import com.example.vm.dto.put.VisitTypePutDTO;
import com.example.vm.service.VisitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return visitTypeService.updateVisitType(id, visitTypeUpdate);
    }

}
