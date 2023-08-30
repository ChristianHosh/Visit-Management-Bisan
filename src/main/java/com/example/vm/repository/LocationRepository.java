package com.example.vm.repository;

import com.example.vm.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location,Long> {
    List<Location> findByEnabledTrue();
    Optional<Location> findByIdAndEnabledTrue(Long id);
}
