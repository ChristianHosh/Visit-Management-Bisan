package com.example.vm.repository;

import com.example.vm.model.City;
import com.example.vm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("select l from Location l where l.enabled = true order by l.city.name")
    List<Location> findLocationsForReport();
    boolean existsByCityAndAddressIgnoreCase(City city, String address);

    List<Location> findByEnabledTrue();

    Optional<Location> findByIdAndEnabledTrue(Long id);
}
