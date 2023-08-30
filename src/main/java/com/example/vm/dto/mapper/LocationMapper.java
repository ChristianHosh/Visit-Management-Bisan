package com.example.vm.dto.mapper;

import com.example.vm.dto.request.LocationRequest;
import com.example.vm.dto.response.LocationResponse;
import com.example.vm.model.City;
import com.example.vm.model.Location;

import java.util.List;

public class LocationMapper {
    public static LocationResponse toResponse(Location location){
        LocationResponse response = new LocationResponse();

        response.setId(location.getId());
        response.setAddress(location.getAddress());
        response.setCityName(location.getCity().getName());
        response.setCityId(location.getCity().getId());

        return response;
    }

    public static List<LocationResponse> toResponseList(List<Location> locationList){
        return locationList
                .stream()
                .map(LocationMapper::toResponse)
                .toList();
    }

    public static Location toEntity(LocationRequest request, City city){
        return Location.builder()
                .address(request.getAddress())
                .city(city)
                .build();

    }
}
