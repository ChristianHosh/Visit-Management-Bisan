package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.request.CityRequest;
import com.example.vm.dto.request.VisitTypeRequest;
import com.example.vm.model.City;
import com.example.vm.model.visit.VisitType;
import com.example.vm.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private final CityRepository cityRepository;

    @Autowired
    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public ResponseEntity<List<City>> findAllEnabledCities() {

        return ResponseEntity.ok(cityRepository.findCityByEnabled(true));
    }
    public ResponseEntity<List<City>> findAll() {

        return ResponseEntity.ok(cityRepository.findAll());
    }

    public ResponseEntity<City> findById(Long id) {
        City foundCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.USER_NOT_FOUND));

        return ResponseEntity.ok(foundCity);
    }

    public ResponseEntity<City> saveNewCity(CityRequest cityRequest) {
        City cityToSave = City.builder()
                .name(cityRequest.getName())
                .build();

        cityToSave = cityRepository.save(cityToSave);

        return ResponseEntity.ok(cityToSave);

    }

    public ResponseEntity<City> updateCity(Long id, CityRequest updatedCity) {
       City foundCity= cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.CITY_NOT_FOUND));

        foundCity.setName(updatedCity.getName());

        foundCity = cityRepository.save(foundCity);
        return ResponseEntity.ok(foundCity);
    }

}