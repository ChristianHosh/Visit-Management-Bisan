package com.example.vm.repository;

import com.example.vm.model.User;
import com.example.vm.model.VisitAssignment;
import com.example.vm.model.VisitDefinition;
import com.example.vm.model.enums.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface VisitAssignmentRepository extends JpaRepository<VisitAssignment, Long> {
    @Query("select v from VisitAssignment v where v.id = ?1 and v.visitDefinition.type.base = com.example.vm.model.enums.VisitTypeBase.QUESTION")
    Optional<VisitAssignment> findByIdAndIsQuestionAssignment(Long id);

    @Query("select v from VisitAssignment v where v.visitDefinition.type.base = com.example.vm.model.enums.VisitTypeBase.QUESTION")
    List<VisitAssignment> findQuestionAssignments();

    @Query("select v from VisitAssignment v " +
            "where v.id = ?1 " +
            "and v.visitDefinition.type.base = com.example.vm.model.enums.VisitTypeBase.PAYMENT " +
            "and v.enabled = true")
    Optional<VisitAssignment> findByIdAndTypeBasePaymentAndEnabledTrue(Long id);

    @Query("SELECT a from VisitAssignment a " +
            "WHERE (" +
            "   (:comment IS NULL OR a.comment LIKE concat('%',:comment,'%')) AND " +
            "   (:userUsername IS NULL OR a.user.username = :userUsername) AND " +
            "   ((:startDate IS NULL OR :endDate IS NULL) OR a.date BETWEEN :startDate AND :endDate) AND " +
            "   (a.visitDefinition.type.base = com.example.vm.model.enums.VisitTypeBase.QUESTION)" +
            ")")
    List<VisitAssignment> searchQuestionAssignments(String comment, String userUsername, java.sql.Date startDate, java.sql.Date endDate);

    List<VisitAssignment> findByUserAndDateBetweenAndEnabledTrue(User user, java.sql.Date dateStart, java.sql.Date dateEnd);

    long countByUserAndDateBetweenAndStatusAndEnabledTrue(User user, java.sql.Date dateStart, java.sql.Date dateEnd, VisitStatus status);

    long countByUserAndDateBetweenAndEnabledTrue(User user, java.sql.Date dateStart, java.sql.Date dateEnd);

    List<VisitAssignment> findByUserAndDateAfter(User user, java.sql.Date date);

    List<VisitAssignment> findByVisitDefinitionAndDateAfter(VisitDefinition visitDefinition, java.sql.Date date);

    Optional<VisitAssignment> findByIdAndEnabledTrue(Long id);

    List<VisitAssignment> findVisitAssignmentsByEnabledTrue();

    List<VisitAssignment> findVisitAssignmentByDateBetween(Date date1, Date date2);

    List<VisitAssignment> findVisitAssignmentByUserAndEnabledTrue(User user);

}
