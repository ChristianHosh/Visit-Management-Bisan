package com.example.vm.dto.mapper;

import com.example.vm.dto.response.AddressResponse;
import com.example.vm.dto.response.CustomerResponse;
import com.example.vm.model.Address;
import com.example.vm.model.Customer;

import java.util.List;

public class CustomerMapper {

    // CUSTOMER
    public static CustomerResponse toDetailedResponse(Customer customer) {
        if (customer == null) return null;

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setAddress(getAddressResponse(customer.getAddress()));
        response.setContacts(ContactMapper.listToResponseList(customer.getContacts()));

        response.setEnabled(customer.getEnabled());
        response.setCreatedTime(customer.getCreatedTime());
        response.setLastModifiedTime(customer.getLastModifiedTime());

        return response;
    }

    public static CustomerResponse toListResponse(Customer customer) {
        if (customer == null) return null;

        CustomerResponse response = new CustomerResponse();

        response.setId(customer.getId());
        response.setName(customer.getName());
        response.setAddress(getAddressResponse(customer.getAddress()));

        response.setEnabled(customer.getEnabled());

        return response;
    }

    public static List<CustomerResponse> listToResponseList(List<Customer> customerList) {
        if (customerList == null) return null;

        return customerList
                .stream()
                .map(CustomerMapper::toListResponse)
                .toList();
    }

    private static AddressResponse getAddressResponse(Address customerAddress) {
        if (customerAddress == null) return null;

        AddressResponse address = new AddressResponse();

        address.setAddressLine1(customerAddress.getAddressLine1());
        address.setAddressLine2(customerAddress.getAddressLine2());
        address.setZipcode(customerAddress.getZipcode());
        address.setLongitude(customerAddress.getLongitude());
        address.setLatitude(customerAddress.getLatitude());
        address.setCityId(customerAddress.getCity().getId());
        address.setCityName(customerAddress.getCity().getName());

        address.setEnabled(customerAddress.getEnabled());
        address.setLastModifiedTime(customerAddress.getLastModifiedTime());
        address.setCreatedTime(customerAddress.getCreatedTime());

        return address;
    }


}
