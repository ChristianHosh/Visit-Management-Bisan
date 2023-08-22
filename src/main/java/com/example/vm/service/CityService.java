package com.example.vm.service;

import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.model.City;
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

    public ResponseEntity<List<City>> findAll() {
        return ResponseEntity.ok(cityRepository.findCityByEnabled(1));
    }

    public ResponseEntity<City> findById(Integer id) {
        System.out.print("hgh");

        City foundCity = cityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));
        return ResponseEntity.ok(foundCity);
    }

    public ResponseEntity<City> saveNewCity(City city) {
        City cityToSave = City.builder()
                .id(city.getId())
                .name(city.getName())
                .enabled(1)
                .build();

        cityToSave = cityRepository.save(cityToSave);

        return ResponseEntity.ok(cityToSave);

    }

}