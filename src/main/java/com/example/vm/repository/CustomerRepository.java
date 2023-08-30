package com.example.vm.repository;

import com.example.vm.model.City;
import com.example.vm.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("""
            select c from Customer c
            where upper(c.name) like upper(concat('%', ?1, '%')) or upper(c.location.addressLine1) like upper(concat('%', ?2, '%')) or upper(c.location.addressLine2) like upper(concat('%', ?3, '%')) or upper(c.location.city.name) like upper(concat('%', ?4, '%'))""")
    List<Customer> findByNameContainsIgnoreCaseOrAddress_AddressLine1ContainsIgnoreCaseOrAddress_AddressLine2ContainsIgnoreCaseOrAddress_City_NameContainsIgnoreCase(String name, String addressLine1, String addressLine2, String name1);
    Optional<Customer> findByIdAndEnabledTrue(Long id);

    @Query("SELECT u FROM Customer u WHERE SIZE(u.visitAssignments) > 0")
    List<Customer> findAllCustomerWhoHaveAssignments();
    // DONE BY ELIANA

    List<Customer> findCustomersByVisitAssignmentsIsNotEmpty();
    List<Customer> findCustomerByEnabledTrue();

    Long countCustomerByEnabled(Boolean enabled);

    Long countCustomerByAddress_CityAndEnabled(City city, Boolean enabled);

}
