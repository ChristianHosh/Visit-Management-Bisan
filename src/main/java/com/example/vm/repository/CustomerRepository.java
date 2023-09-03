package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByLocation(Location location);
    List<Customer> findCustomerByEnabledTrueAndLocation(Location location);

    long countByEnabledTrue();

    long countByLocationAndEnabledTrue(Location location);

    List<Customer> findByNameContainsIgnoreCaseAndLocation_AddressContainsIgnoreCaseAndLocation_City_NameContainsIgnoreCase(String name, String address, String name1);

    Optional<Customer> findByIdAndEnabledTrue(Long id);

    @Query("SELECT u FROM Customer u WHERE SIZE(u.visitAssignments) > 0")
    List<Customer> findAllCustomerWhoHaveAssignments();
    // DONE BY ELIANA

    List<Customer> findCustomersByVisitAssignmentsIsNotEmpty();

    List<Customer> findCustomerByEnabledTrue();


}
