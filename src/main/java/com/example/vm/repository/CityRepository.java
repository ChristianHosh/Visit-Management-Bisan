package com.example.vm.repository;

import com.example.vm.model.City;
import com.example.vm.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
 public interface CityRepository extends JpaRepository<City, Integer> {

 }
