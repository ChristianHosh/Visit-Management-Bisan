package com.example.vm.dto.mapper;

import com.example.vm.dto.request.CustomerRequest;
import com.example.vm.dto.response.LocationResponse;
import com.example.vm.dto.response.CustomerResponse;
import com.example.vm.model.GeoCoordinates;
import com.example.vm.model.Location;
import com.example.vm.model.Customer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CustomerMapper {

    // CUSTOMER
    public static CustomerResponse toDetailedResponse(Customer customer) {
        if (customer == null) return null;

        CustomerResponse response = setBasicAttributes(customer);

        response.setContacts(ContactMapper.listToResponseList(customer.getContacts()));

        return response;
    }

    public static CustomerResponse toListResponse(Customer customer) {
        if (customer == null) return null;

        return setBasicAttributes(customer);
    }

    public static List<CustomerResponse> listToResponseList(List<Customer> customerList) {
        if (customerList == null) return null;

        return customerList
                .stream()
                .map(CustomerMapper::toListResponse)
                .toList();
    }

    private static LocationResponse getLocationResponse(Location customerLocation) {
        if (customerLocation == null) return null;

        LocationResponse response = new LocationResponse();

        response.setId(customerLocation.getId());
        response.setAddress(customerLocation.getAddress());
        response.setCityId(customerLocation.getCity().getId());
        response.setCityName(customerLocation.getCity().getName());

        response.setEnabled(customerLocation.getEnabled());
        response.setLastModifiedTime(customerLocation.getLastModifiedTime());
        response.setCreatedTime(customerLocation.getCreatedTime());

        return response;
    }

    @NotNull
    private static CustomerResponse setBasicAttributes(Customer customer) {
        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setLatitude(customer.getGeoCoordinates().getLatitude());
        response.setLongitude(customer.getGeoCoordinates().getLongitude());
        response.setLocation(getLocationResponse(customer.getLocation()));

        response.setEnabled(customer.getEnabled());
        response.setCreatedTime(customer.getCreatedTime());
        response.setLastModifiedTime(customer.getLastModifiedTime());
        return response;
    }

    public static Customer toEntity(CustomerRequest customerRequest, Location foundLocation) {
        GeoCoordinates geoCoordinates = GeoCoordinates
                .builder()
                .latitude(customerRequest.getLatitude())
                .longitude(customerRequest.getLongitude())
                .build();

        return Customer.builder()
                .name(customerRequest.getName())
                .geoCoordinates(geoCoordinates)
                .location(foundLocation)
                .visitAssignments(new ArrayList<>())
                .build();

    }

    public static void update(Customer oldCustomer, CustomerRequest customerRequest, Location foundLocation) {
        if (oldCustomer.getGeoCoordinates() == null)
            oldCustomer.setGeoCoordinates(new GeoCoordinates());

        oldCustomer.setName(customerRequest.getName());
        oldCustomer.getGeoCoordinates().setLongitude(customerRequest.getLongitude());
        oldCustomer.getGeoCoordinates().setLatitude(customerRequest.getLatitude());
        oldCustomer.setLocation(foundLocation);
    }
}
