package com.example.vm.service;


import com.example.vm.model.User;
import com.example.vm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public List<User> findAllUsers() {
        return repository.findAll();
    }

    public User findUserByUsername(String username) {
        Optional<User> userOptional = repository.findById(username);

        return userOptional.orElse(null);
    }


    public List<User> searchUsersByFirstName(String firstName) {
        return repository.searchUsersByFirstNameContaining(firstName);
    }

    public List<User> searchUsersByLastName(String firstName) {
        return repository.searchUsersByLastNameContaining(firstName);
    }

    public List<User> searchUsersByAccessLevel(int accessLevel) {
        return repository.searchUsersByAccessLevel(accessLevel);
    }


    public User saveNewUser(User userToSave) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        userToSave.setCreatedTime(timestamp);
        userToSave.setLastModifiedTime(timestamp);
        userToSave.setEnabled(1);

        userToSave.setFirstName(userToSave.getFirstName().trim());
        userToSave.setLastName(userToSave.getLastName().trim());

        userToSave.setUsername(userToSave.getUsername().trim().toLowerCase());
        userToSave.setPassword(userToSave.getPassword().trim());

        return repository.save(userToSave);
    }


    public User updateUser(User userToUpdate, User updatedUser) {
        userToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        userToUpdate.setFirstName(updatedUser.getFirstName().trim());
        userToUpdate.setLastName(updatedUser.getLastName().trim());

        userToUpdate.setAccessLevel(updatedUser.getAccessLevel());

        return repository.save(userToUpdate);
    }

    public User disableUser(User user) {
        user.setEnabled(0);

        return repository.save(user);
    }
}
