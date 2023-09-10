package com.example.vm.repository;

import com.example.vm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByAccessLevel(Integer accessLevel);

    List<User> findByEnabled(Boolean enabled);

    List<User> findByUsernameContainsOrFirstNameContainsOrLastNameContainsAllIgnoreCase(String username, String firstName, String lastName);

    List<User> findByAccessLevelAndEnabledTrue(Integer accessLevel);

    List<User> searchUsersByFirstNameContainingOrLastNameContainingOrUsernameContaining(String firstName, String lastName, String username);

    Optional<User> findUserByUsernameAndEnabledTrue(String username);

    List<User> findUsersByEnabledTrue();

    List<User> searchUsersByAccessLevelAndEnabledTrue(Integer accessLevel);

    Optional<User> findByUsernameAndPassword(String username, String password);
}
