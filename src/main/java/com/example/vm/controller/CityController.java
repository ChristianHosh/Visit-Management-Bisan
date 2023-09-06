package com.example.vm.controller;

import com.example.vm.dto.request.SimpleNameRequest;
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
    public ResponseEntity<?> getAllEnableCities() {
        return cityService.findAllEnabledCities();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllCity() {
        return cityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCityById(@PathVariable @Valid Long id) {
        return cityService.findById(id);
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewCity(@RequestBody @Valid SimpleNameRequest cityRequest) {
        return cityService.saveNewCity(cityRequest);
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> saveLocationToCity(@PathVariable Long id, @RequestBody @Valid SimpleNameRequest locationRequest) {
        return cityService.saveNewLocationToCity(id, locationRequest);
    }
}
