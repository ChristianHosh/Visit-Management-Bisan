package com.example.vm.repository;

import com.example.vm.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    Optional<City> findByIdAndEnabledTrue(Long id);
    List<City> findCitiesByEnabledTrue();

}
