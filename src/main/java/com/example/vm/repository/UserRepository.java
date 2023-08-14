package com.example.vm.repository;

import com.example.vm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    List<User> searchUsersByFirstNameContainingOrLastNameContainingOrAccessLevel(String firstName, String lastName, int accessLevel);


}
