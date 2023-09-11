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

//
//    @Query("SELECT u FROM User u " +
//            "WHERE ((:name IS NULL OR u.username like concat('%', :name, '%'))" +
//            "OR (:name IS NULL OR u.firstName like concat('%', :name, '%'))" +
//            "OR (:name IS NULL OR u.lastName like concat('%', :name, '%')))" +
//            "AND (:role IS NULL OR u.accessLevel = :role)" +
//            "AND (:enabled IS NULL OR u.enabled = :enabled)")
    @Query("SELECT c FROM Customer c " +
            "WHERE (:name IS NULL OR c.name like concat('%', :name, '%')) " +
            "AND (:enabled IS NULL OR c.enabled = :enabled) " +
            "AND (:cityId IS NULL OR c.location.city.id = :cityId) " +
            "AND (:locationId IS NULL OR c.location.id = :locationId)")
    List<Customer> searchCustomers(String name, Boolean enabled, Long cityId, Long locationId);
}
