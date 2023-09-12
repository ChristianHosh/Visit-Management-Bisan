package com.example.vm.repository;

import com.example.vm.model.PasswordReset;
import com.example.vm.model.User;
import com.example.vm.model.enums.PasswordStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
//    @Query("select (count(p) > 0) from PasswordReset p " +
//            "where p.user = ?1 " +
//            "and (p.createdTime > ?2 or p.status = ?3)")
    @Query("SELECT (COUNT(p) > 0) FROM PasswordReset p " +
            "WHERE p.user = :user " +
            "AND ( " +
            "  (p.status = com.example.vm.model.enums.PasswordStatus.PENDING) OR " +
            "  (" +
            "       (p.status = com.example.vm.model.enums.PasswordStatus.ACCEPTED) OR " +
            "       (p.status = com.example.vm.model.enums.PasswordStatus.REJECTED) AND " +
            "   p.createdTime > :timestamp)" +
            ") ")
    boolean isValidResetRequest(User user, Timestamp timestamp);

    @Query("SELECT (COUNT(p) > 0) FROM PasswordReset p " +
            "WHERE p.user = :user " +
            "AND p.status = com.example.vm.model.enums.PasswordStatus.PENDING")
    boolean isRequestStillPending(User user);

    @Query("SELECT (COUNT(p) > 0) FROM PasswordReset p " +
            "WHERE p.user = :user " +
            "AND (" +
            "   (p.status = com.example.vm.model.enums.PasswordStatus.ACCEPTED) OR " +
            "   (p.status = com.example.vm.model.enums.PasswordStatus.REJECTED) AND " +
            "p.createdTime > :timestamp)")
    boolean isRequestStillEarly(User user, Timestamp timestamp);
    Optional<PasswordReset> findByIdAndStatus(Long id, PasswordStatus status);

    List<PasswordReset> findByStatus(PasswordStatus status);

}
