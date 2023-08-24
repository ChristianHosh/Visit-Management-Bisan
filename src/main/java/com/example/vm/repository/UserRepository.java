package com.example.vm.repository;

import com.example.vm.model.Customer;
import com.example.vm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> searchUsersByFirstNameContainingOrLastNameContainingOrUsernameContaining(String firstName, String lastName, String username);

    List<User> findUsersByEnabled(Boolean enabled);
    List<User> searchUsersByAccessLevel(int accessLevel);

    List<User> searchUsersByAccessLevelAndEnabled(int accessLevel, Boolean enabled);
}
