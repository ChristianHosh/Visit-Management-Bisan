package com.example.vm.repository;

import com.example.vm.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
 public interface CityRepository extends JpaRepository<City, Long> {
 List<City> findCityByEnabled(Boolean enabled);

 }
