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
        if (isAccessLevelNotValid(accessLevel))
            return null;

        return repository.searchUsersByAccessLevel(accessLevel);

    }

    public User saveNewUser(User userToSave) {
        Optional<User> userOptional = repository.findById(userToSave.getUsername());

        if (userOptional.isPresent())
            return null;

        if (isAccessLevelNotValid(userToSave.getAccessLevel()))
            return null;

        if (isEnabledNotValid(userToSave.getEnabled()))
            return null;

        if (isNameNotValid(userToSave.getFirstName().trim()))
            return null;

        if (isNameNotValid(userToSave.getLastName().trim()))
            return null;

        userToSave.setCreatedTime(Timestamp.from(Instant.now()));
        userToSave.setLastModifiedTime(Timestamp.from(Instant.now()));
        userToSave.setEnabled(1);

        userToSave.setFirstName(userToSave.getFirstName().trim());
        userToSave.setLastName(userToSave.getLastName().trim());

        userToSave.setUsername(userToSave.getUsername().trim().toLowerCase());
        userToSave.setPassword(userToSave.getPassword().trim());


        return repository.save(userToSave);
    }


    public User updateUser(String username, User updatedUser) {
        Optional<User> userOptional = repository.findById(username);

        if (userOptional.isEmpty())
            return null;

        if (isAccessLevelNotValid(updatedUser.getAccessLevel()))
            return null;

        if (isEnabledNotValid(updatedUser.getEnabled()))
            return null;

        if (isNameNotValid(updatedUser.getFirstName().trim()))
            return null;

        if (isNameNotValid(updatedUser.getLastName().trim()))
            return null;


        User userToUpdate = userOptional.get();

        userToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        userToUpdate.setFirstName(updatedUser.getFirstName().trim());
        userToUpdate.setLastName(updatedUser.getLastName().trim());

        userToUpdate.setAccessLevel(updatedUser.getAccessLevel());
        userToUpdate.setEnabled(updatedUser.getEnabled());

        return repository.save(userToUpdate);
    }


    private boolean isAccessLevelNotValid(int accessLevel) {
        return (accessLevel != 1) && (accessLevel != 0);
    }

    private boolean isEnabledNotValid(int enabled) {
        return (enabled != 1) && (enabled != 0);
    }

    public boolean isNameNotValid(String name) {
        return !name.matches("[a-zA-Z ]+");
    }

}
