package com.example.vm.controller;

import com.example.vm.dto.request.CityRequest;
import com.example.vm.service.CityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/cities")
public class CityController {
    private final CityService cityService;

    @Autowired
    public CityController(CityService cityService) {
        this.cityService = cityService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllCity() {
        return cityService.findAll();
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewCity(@RequestBody @Valid CityRequest cityRequest) {
        return cityService.saveNewCity(cityRequest);
    }
}
