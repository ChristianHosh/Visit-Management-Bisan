package com.example.vm.controller;

import com.example.vm.model.City;
import com.example.vm.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/city")
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
    public ResponseEntity<?> saveNewCity(@RequestBody City city) {
        return cityService.saveNewCity(city);
    }
}
