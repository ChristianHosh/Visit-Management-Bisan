package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.User;
import com.example.vm.model.visit.VisitDefinition;
import com.example.vm.model.visit.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitDefinitionRepository extends JpaRepository<VisitDefinition, Long> {
    Optional<VisitDefinition> findVisitDefinitionByIdAndEnabled(Long id, boolean enable);

    List<VisitDefinition> searchVisitDefinitionsByNameContaining(String name);

    List<VisitDefinition> searchVisitDefinitionsByFrequency(int frequency);

    List<VisitDefinition> searchVisitDefinitionsByType(VisitType visitType);

    Long countVisitDefinitionsByTypeAndEnabled(VisitType visitType, boolean enable);

    List<VisitDefinition> findVisitDefinitionsByEnabled(Boolean enabled);

    long countVisitDefinitionsByEnabled(Boolean enabled);
}