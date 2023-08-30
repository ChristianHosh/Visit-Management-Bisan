package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.LocationMapper;
import com.example.vm.dto.request.LocationRequest;
import com.example.vm.dto.response.LocationResponse;
import com.example.vm.model.City;
import com.example.vm.model.Location;
import com.example.vm.repository.CityRepository;
import com.example.vm.repository.LocationRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LocationService {
    private final LocationRepository locationRepository;
    private final CityRepository cityRepository;

    public LocationService(LocationRepository locationRepository,
                           CityRepository cityRepository) {
        this.locationRepository = locationRepository;
        this.cityRepository = cityRepository;
    }

    public ResponseEntity<LocationResponse> saveNewLocation(@Valid LocationRequest locationRequest) {
        City foundCity = cityRepository.findById(locationRequest.getCityId())
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        Location locationToSave = LocationMapper.toEntity(locationRequest, foundCity);

        locationToSave = locationRepository.save(locationToSave);

        return ResponseEntity.ok(LocationMapper.toResponse(locationToSave));


    }
}
