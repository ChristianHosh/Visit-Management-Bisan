package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.CustomerMapper;
import com.example.vm.dto.mapper.LocationMapper;
import com.example.vm.dto.response.CustomerResponse;
import com.example.vm.dto.response.LocationResponse;
import com.example.vm.model.Customer;
import com.example.vm.model.Location;
import com.example.vm.repository.CustomerRepository;
import com.example.vm.repository.LocationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final CustomerRepository customerRepository;

    public LocationService(LocationRepository locationRepository,
                           CustomerRepository customerRepository) {
        this.locationRepository = locationRepository;
        this.customerRepository = customerRepository;
    }

    public ResponseEntity<List<CustomerResponse>> findLocationCustomers(Long id) {
        Location foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));

        List<Customer> customerList = customerRepository.findByLocation(foundLocation);

        return ResponseEntity.ok(CustomerMapper.listToResponseList(customerList));
    }

    public ResponseEntity<LocationResponse> updateLocationCoordinates(Long id){
        Location foundLocation = locationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.LOCATION_NOT_FOUND));

        LocationMapper.updateGeoCoordinates(foundLocation);
        System.out.println(foundLocation.getGeoCoordinates());
        locationRepository.save(foundLocation);

        return ResponseEntity.ok(LocationMapper.toResponse(foundLocation));
    }
}
