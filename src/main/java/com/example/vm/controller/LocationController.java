package com.example.vm.controller;

import com.example.vm.service.LocationService;
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


    @GetMapping("/{id}/customers")
    public ResponseEntity<?> findLocationCustomers(@PathVariable Long id) {
        return locationService.findLocationCustomers(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLocationCoordinates(@PathVariable Long id) {
        return locationService.updateLocationCoordinates(id);
    }

}

