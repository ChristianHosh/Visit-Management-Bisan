package com.example.vm.repository;

import com.example.vm.model.User;
import com.example.vm.model.visit.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitTypeRepository extends JpaRepository<VisitType, Long> {
    List<VisitType> findVisitTypesByEnabled(Boolean enabled);
}
