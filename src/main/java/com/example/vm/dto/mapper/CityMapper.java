package com.example.vm.dto.mapper;

import com.example.vm.dto.request.SimpleNameRequest;
import com.example.vm.dto.response.CityResponse;
import com.example.vm.model.City;

import java.util.List;

public class CityMapper {

    public static CityResponse toListResponse(City city){
        CityResponse response = new CityResponse();

        response.setId(city.getId());
        response.setName(city.getName());
        response.setLocations(LocationMapper.toResponseList(city.getLocations()));

        response.setEnabled(city.getEnabled());
        response.setCreatedTime(city.getCreatedTime());
        response.setLastModifiedTime(city.getLastModifiedTime());

        return response;
    }

    public static List<CityResponse> toResponseList(List<City> cityList){
        return cityList
                .stream()
                .map(CityMapper::toListResponse)
                .toList();
    }

    public static City toEntity(SimpleNameRequest request){
        return City.builder()
                .name(request.getName().trim())
                .build();
    }


}
