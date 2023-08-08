package com.example.vm.service;

import com.example.vm.dto.put.AddressPutDTO;
import com.example.vm.model.Address;
import com.example.vm.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class AddressService {

    private final AddressRepository repository;

    @Autowired
    public AddressService(AddressRepository repository) {
        this.repository = repository;
    }

    public Address updateCustomerAddress(Address addressToUpdate, AddressPutDTO updatedDTO) {

        addressToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        addressToUpdate.setAddressLine1(updatedDTO.getAddressLine1());
        addressToUpdate.setAddressLine2(updatedDTO.getAddressLine2());
        addressToUpdate.setCity(updatedDTO.getCity());
        addressToUpdate.setLatitude(updatedDTO.getLatitude());
        addressToUpdate.setLongitude(updatedDTO.getLongitude());
        addressToUpdate.setZipcode(updatedDTO.getZipcode());

        return repository.save(addressToUpdate);
    }

}
