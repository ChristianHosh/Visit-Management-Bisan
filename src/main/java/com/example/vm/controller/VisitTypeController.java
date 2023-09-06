package com.example.vm.controller;

import com.example.vm.dto.request.SimpleNameRequest;
import com.example.vm.service.VisitTypeService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/visit_types")
public class VisitTypeController {
    private final VisitTypeService visitTypeService;

    public VisitTypeController(VisitTypeService visitTypeService) {
        this.visitTypeService = visitTypeService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllEnableVisitTypes() {
        return visitTypeService.findAllEnablesTypes();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllVisitTypes() {
        return visitTypeService.findAll();
    }
    @PostMapping("")
    public ResponseEntity<?> saveNewVisitType(@RequestBody @Valid SimpleNameRequest simpleNameRequest) {
        System.out.println("---->" + simpleNameRequest);
        return visitTypeService.saveNewVisitType(simpleNameRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVisitType(@PathVariable Long id, @RequestBody @Valid SimpleNameRequest simpleNameRequest) {
        System.out.println("---->" + simpleNameRequest);
        return visitTypeService.updateVisitType(id, simpleNameRequest);
    }

}
