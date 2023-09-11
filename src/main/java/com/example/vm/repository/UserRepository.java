package com.example.vm.repository;

import com.example.vm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    List<User> findByAccessLevelAndEnabledTrue(Integer accessLevel);

    Optional<User> findUserByUsernameAndEnabledTrue(String username);

    List<User> findUsersByEnabledTrue();

    List<User> searchUsersByAccessLevelAndEnabledTrue(Integer accessLevel);

    Optional<User> findByUsernameAndPassword(String username, String password);

    @Query("select u from User u where u.lastName like concat('%', ?1, '%')")
    List<User> findByLastNameContains(String lastName);

    @Query("SELECT u FROM User u " +
            "WHERE ((:name IS NULL OR u.username like concat('%', :name, '%'))" +
            "OR (:name IS NULL OR u.firstName like concat('%', :name, '%'))" +
            "OR (:name IS NULL OR u.lastName like concat('%', :name, '%')))" +
            "AND (:role IS NULL OR u.accessLevel = :role)" +
            "AND (:enabled IS NULL OR u.enabled = :enabled)")
    List<User> searchUsers(String name, Boolean enabled, Integer role);
}
