package com.example.vm.dto.mapper;

import com.example.vm.dto.request.CustomerRequest;
import com.example.vm.dto.response.AddressResponse;
import com.example.vm.dto.response.CustomerResponse;
import com.example.vm.model.Location;
import com.example.vm.model.City;
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

    private static AddressResponse getAddressResponse(Location customerLocation) {
        if (customerLocation == null) return null;

        AddressResponse address = new AddressResponse();

        address.setAddressLine1(customerLocation.getAddressLine1());
        address.setAddressLine2(customerLocation.getAddressLine2());
        address.setZipcode(customerLocation.getZipcode());
        address.setLongitude(customerLocation.getLongitude());
        address.setLatitude(customerLocation.getLatitude());
        address.setCityId(customerLocation.getCity().getId());
        address.setCityName(customerLocation.getCity().getName());

        address.setEnabled(customerLocation.getEnabled());
        address.setLastModifiedTime(customerLocation.getLastModifiedTime());
        address.setCreatedTime(customerLocation.getCreatedTime());

        return address;
    }

    @NotNull
    private static CustomerResponse setBasicAttributes(Customer customer) {
        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setAddress(getAddressResponse(customer.getLocation()));

        response.setEnabled(customer.getEnabled());
        response.setCreatedTime(customer.getCreatedTime());
        response.setLastModifiedTime(customer.getLastModifiedTime());
        return response;
    }

    public static Customer toEntity(CustomerRequest customerRequest, City foundCity) {
        return Customer.builder()
                .name(customerRequest.getName())
                .visitAssignments(new ArrayList<>())
                .location(Location.builder()
                        .addressLine1(customerRequest.getAddressLine1())
                        .addressLine2(customerRequest.getAddressLine2())
                        .longitude(customerRequest.getLongitude())
                        .latitude(customerRequest.getLatitude())
                        .zipcode(customerRequest.getZipcode())
                        .city(foundCity)
                        .build())
                .build();
    }

    public static void update(Customer oldCustomer, CustomerRequest customerRequest, City foundCity) {
        oldCustomer.setName(customerRequest.getName());

        oldCustomer.getLocation().setAddressLine1(customerRequest.getAddressLine1());
        oldCustomer.getLocation().setAddressLine2(customerRequest.getAddressLine2());
        oldCustomer.getLocation().setZipcode(customerRequest.getZipcode());
        oldCustomer.getLocation().setCity(foundCity);

    }
}
