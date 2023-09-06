package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.dto.mapper.CityMapper;
import com.example.vm.dto.mapper.LocationMapper;
import com.example.vm.dto.request.SimpleNameRequest;
import com.example.vm.dto.response.CityResponse;
import com.example.vm.dto.response.LocationResponse;
import com.example.vm.model.City;
import com.example.vm.model.Location;
import com.example.vm.repository.CityRepository;
import com.example.vm.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final LocationRepository locationRepository;

    @Autowired
    public CityService(CityRepository cityRepository,
                       LocationRepository locationRepository) {
        this.cityRepository = cityRepository;
        this.locationRepository = locationRepository;
    }

    public ResponseEntity<List<CityResponse>> findAllEnabledCities() {
        List<City> queryResult = cityRepository.findCitiesByEnabledTrue();

        return ResponseEntity.ok(CityMapper.toResponseList(queryResult));
    }

    public ResponseEntity<List<CityResponse>> findAll() {
        List<City> queryResult = cityRepository.findAll();

        return ResponseEntity.ok(CityMapper.toResponseList(queryResult));
    }

    public ResponseEntity<CityResponse> findById(Long id) {
        City foundCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        return ResponseEntity.ok(CityMapper.toListResponse(foundCity));
    }

    public ResponseEntity<CityResponse> saveNewCity(SimpleNameRequest cityRequest) {
        if (cityRepository.existsByNameIgnoreCase(cityRequest.getName().trim()))
            throw new UserAlreadyExistsException();

        City cityToSave = CityMapper.toEntity(cityRequest);

        cityToSave = cityRepository.save(cityToSave);

        return ResponseEntity.ok(CityMapper.toListResponse(cityToSave));
    }

    public ResponseEntity<LocationResponse> saveNewLocationToCity(Long id, SimpleNameRequest simpleNameRequest) {
        City foundCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        if (locationRepository.existsByCityAndAddressIgnoreCase(foundCity, simpleNameRequest.getName().trim()))
            throw new UserAlreadyExistsException();

        Location locationToSave = LocationMapper.toEntity(simpleNameRequest, foundCity);

        locationToSave = locationRepository.save(locationToSave);

        return ResponseEntity.ok(LocationMapper.toResponse(locationToSave));
    }
}