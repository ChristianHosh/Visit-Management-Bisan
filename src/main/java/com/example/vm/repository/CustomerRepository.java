package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    long countByCreatedTimeBetween(Timestamp createdTimeStart, Timestamp createdTimeEnd);

    List<Customer> findByLocation(Location location);

    List<Customer> findCustomerByEnabledTrueAndLocation(Location location);

    long countByEnabledTrue();

    long countByLocationAndEnabledTrue(Location location);

    Optional<Customer> findByIdAndEnabledTrue(Long id);

    @Query("SELECT u FROM Customer u WHERE SIZE(u.visitAssignments) > 0")
    List<Customer> findAllCustomerWhoHaveAssignments();
    // DONE BY ELIANA

    List<Customer> findCustomersByVisitAssignmentsIsNotEmpty();

    List<Customer> findCustomerByEnabledTrue();

    @Query("SELECT c FROM Customer c " +
            "WHERE (:name IS NULL OR c.name like concat('%', :name, '%')) " +
            "AND (:enabled IS NULL OR c.enabled = :enabled) " +
            "AND (:cityId IS NULL OR c.location.city.id = :cityId) " +
            "AND (:locationId IS NULL OR c.location.id = :locationId)")
    List<Customer> searchCustomers(String name, Boolean enabled, Long cityId, Long locationId);
}
