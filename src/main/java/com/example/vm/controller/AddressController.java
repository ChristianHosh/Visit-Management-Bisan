package com.example.vm.controller;


import com.example.vm.model.Address;
import com.example.vm.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressRepository repository;

    @Autowired
    public AddressController(AddressRepository repository) {
        this.repository = repository;
    }

    @PostMapping("")
    public Address saveNewLocation(@RequestBody Address addressToSave) {

        System.out.println("HERE");

        Timestamp timestamp = Timestamp.from(Instant.now());
        addressToSave.setCreatedTime(timestamp);
        addressToSave.setLastModifiedTime(timestamp);
        return repository.save(addressToSave);
    }
}
