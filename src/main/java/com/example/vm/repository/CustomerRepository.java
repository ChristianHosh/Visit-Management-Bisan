package com.example.vm.repository;

import com.example.vm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    List<Customer> searchCustomersByNameContaining(String name);

    List<Customer> searchCustomersByAddress_CityContaining(String city);

    List<Customer> searchCustomersByAddress_AddressLine1ContainingOrAddress_AddressLine2Containing(String addressLine1, String addressLine2);
}
