package com.example.vm.repository;

import com.example.vm.model.PasswordReset;
import com.example.vm.model.User;
import com.example.vm.model.enums.PasswordStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Long> {
    Optional<PasswordReset> findByIdAndStatus(Long id, PasswordStatus status);
    List<PasswordReset> findByStatus(PasswordStatus status);
    boolean existsByUserAndCreatedTimeAfterOrStatusNot(User user, Timestamp createdTime, PasswordStatus status);

}
