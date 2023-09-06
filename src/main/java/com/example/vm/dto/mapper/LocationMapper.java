package com.example.vm.dto.mapper;

import com.example.vm.controller.error.exception.LocationNotFoundException;
import com.example.vm.dto.request.SimpleNameRequest;
import com.example.vm.dto.response.LocationResponse;
import com.example.vm.model.City;
import com.example.vm.model.GeoCoordinates;
import com.example.vm.model.Location;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.util.List;

public class LocationMapper {

    private final static String GOOGLE_KEY = "AIzaSyC1rCFrBqu32lHImkYyDBSyfmaxp5YCPao";

    public static LocationResponse toResponse(Location location) {
        LocationResponse response = new LocationResponse();

        response.setId(location.getId());
        response.setAddress(location.getAddress());
        response.setCityName(location.getCity().getName());
        response.setCityId(location.getCity().getId());

        if (location.getGeoCoordinates() != null) {
            response.setLongitude(location.getGeoCoordinates().getLongitude());
            response.setLatitude(location.getGeoCoordinates().getLatitude());
        }

        response.setEnabled(location.getEnabled());
        response.setCreatedTime(location.getCreatedTime());
        response.setLastModifiedTime(location.getLastModifiedTime());

        return response;
    }

    public static List<LocationResponse> toResponseList(List<Location> locationList) {
        return locationList
                .stream()
                .map(LocationMapper::toResponse)
                .toList();
    }

    public static Location toEntity(SimpleNameRequest request, City city) {
        try {
            Double[] latLngLiteral = getGeoCoordinates(request.getName() + " " + city.getName());

            GeoCoordinates geoCoordinates = GeoCoordinates
                    .builder()
                    .latitude(latLngLiteral[0])
                    .longitude(latLngLiteral[1])
                    .build();

            return Location.builder()
                    .address(request.getName().trim())
                    .geoCoordinates(geoCoordinates)
                    .city(city)
                    .build();

        } catch (IOException | InterruptedException | ApiException e) {
            throw new LocationNotFoundException();
        }
    }

    public static void updateGeoCoordinates(Location oldLocation) {
        try {
            if (oldLocation.getGeoCoordinates() == null)
                oldLocation.setGeoCoordinates(new GeoCoordinates());

            Double[] latLngLiteral = getGeoCoordinates(oldLocation.getDetailedLocation());

            oldLocation.getGeoCoordinates().setLongitude(latLngLiteral[1]);
            oldLocation.getGeoCoordinates().setLatitude(latLngLiteral[0]);

        } catch (IOException | InterruptedException | ApiException e) {
            throw new LocationNotFoundException();
        }
    }

    private static Double[] getGeoCoordinates(String address) throws com.google.maps.errors.ApiException, InterruptedException, java.io.IOException, LocationNotFoundException {
        try (GeoApiContext context = new GeoApiContext.Builder().apiKey(GOOGLE_KEY).build()) {
            GeocodingResult[] geocodingResults = GeocodingApi
                    .geocode(context, address + " West Bank, Palestine")
                    .await();

            if (geocodingResults.length == 0)
                throw new LocationNotFoundException();

            return new Double[]{
                    geocodingResults[0].geometry.location.lat,
                    geocodingResults[0].geometry.location.lng,
            };
        }
    }

}
