package com.example.vm.repository;

import com.example.vm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    List<Customer> searchCustomersByNameContainingOrAddress_CityContainingOrAddress_AddressLine1ContainingOrAddress_AddressLine2Containing(String name, String addressLine1, String addressLine2, String city);

    @Query("SELECT u FROM Customer u WHERE SIZE(u.visitAssignments)>0 ")
    List<Customer> findAllCustomer();

    List<Customer> findCustomerByEnabled(int enable);

}
