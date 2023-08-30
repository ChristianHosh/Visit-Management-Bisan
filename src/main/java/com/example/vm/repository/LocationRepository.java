package com.example.vm.repository;

import com.example.vm.model.City;
import com.example.vm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
    Optional<Location> findByIdAndEnabledTrue(Long id);
}
