package com.example.vm.dto.mapper;

import com.example.vm.dto.request.CityRequest;
import com.example.vm.dto.response.CityResponse;
import com.example.vm.model.City;

import java.util.List;

public class CityMapper {

    public static CityResponse toResponse(City city){
        CityResponse response = new CityResponse();

        response.setId(city.getId());
        response.setName(city.getName());

        response.setEnabled(city.getEnabled());
        response.setCreatedTime(city.getCreatedTime());
        response.setLastModifiedTime(city.getLastModifiedTime());

        return response;
    }

    public static List<CityResponse> toResponseList(List<City> cityList){
        return cityList
                .stream()
                .map(CityMapper::toResponse)
                .toList();
    }

    public static City toEntity(CityRequest request){
        return City.builder()
                .name(request.getName())
                .build();
    }
}
