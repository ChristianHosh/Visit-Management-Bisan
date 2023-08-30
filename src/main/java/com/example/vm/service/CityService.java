package com.example.vm.service;

import com.example.vm.controller.error.ErrorMessage;
import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.dto.mapper.CityMapper;
import com.example.vm.dto.request.CityRequest;
import com.example.vm.dto.response.CityResponse;
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

        return ResponseEntity.ok(CityMapper.toResponse(foundCity));
    }

    public ResponseEntity<CityResponse> saveNewCity(CityRequest cityRequest) {
        City cityToSave = CityMapper.toEntity(cityRequest);

        cityToSave = cityRepository.save(cityToSave);

        return ResponseEntity.ok(CityMapper.toResponse(cityToSave));

    }

}