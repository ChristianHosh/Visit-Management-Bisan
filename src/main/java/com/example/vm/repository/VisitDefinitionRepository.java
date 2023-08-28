package com.example.vm.repository;

import com.example.vm.model.VisitDefinition;
import com.example.vm.model.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitDefinitionRepository extends JpaRepository<VisitDefinition, Long> {
    Optional<VisitDefinition> findVisitDefinitionByIdAndEnabledTrue(Long id);

    List<VisitDefinition> searchVisitDefinitionsByNameContaining(String name);

    List<VisitDefinition> searchVisitDefinitionsByFrequency(Integer frequency);

    List<VisitDefinition> searchVisitDefinitionsByType(VisitType visitType);

    List<VisitDefinition> findVisitDefinitionsByEnabledTrue();

    Long countVisitDefinitionsByTypeAndEnabledTrue(VisitType visitType);

    Long countVisitDefinitionsByEnabledTrue();
}