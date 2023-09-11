package com.example.vm.repository;

import com.example.vm.model.VisitDefinition;
import com.example.vm.model.VisitType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VisitDefinitionRepository extends JpaRepository<VisitDefinition, Long> {
    Optional<VisitDefinition> findVisitDefinitionByIdAndEnabledTrue(Long id);

    List<VisitDefinition> findVisitDefinitionsByEnabledTrue();

    Long countVisitDefinitionsByTypeAndEnabledTrue(VisitType visitType);

    Long countVisitDefinitionsByEnabledTrue();

    @Query("SELECT v FROM VisitDefinition v " +
            "WHERE ((:name IS NULL OR v.name like concat('%', :name, '%')) " +
            "OR (:name IS NULL OR v.description like concat('%', :name, '%'))) " +
            "AND (:enabled IS NULL OR v.enabled = :enabled) " +
            "AND (:recurring IS NULL OR v.allowRecurring = :recurring) " +
            "AND (:typeId IS NULL OR v.type.id = :typeId) " +
            "AND (:cityId IS NULL OR v.location.city.id = :cityId) " +
            "AND (:locationId IS NULL OR v.location.id = :locationId)")
    List<VisitDefinition> searchVisitDefinitions(String name, Boolean enabled, Boolean recurring, Long typeId, Long cityId, Long locationId);
}