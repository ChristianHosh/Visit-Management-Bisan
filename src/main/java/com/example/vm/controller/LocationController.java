package com.example.vm.controller;

import com.example.vm.dto.request.LocationRequest;
import com.example.vm.service.LocationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @PostMapping("")
    public ResponseEntity<?> saveNewUser(@RequestBody @Valid LocationRequest locationRequest) {
        return locationService.saveNewLocation(locationRequest);
    }

    @GetMapping("/{id}/customers")
    public ResponseEntity<?> findLocationCustomers(@PathVariable Long id){
        return locationService.findLocationCustomers(id);
    }

}

