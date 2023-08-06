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

    public List<User> findAll() {
        return repository.findAll();
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = repository.findById(username);
        return userOptional.orElse(null);
    }

    public User saveNewUser(User userToSave) {
        Optional<User> userOptional = repository.findById(userToSave.getUsername());

        if (userOptional.isPresent())
            return null;

        if (isAccessLevelValid(userToSave.getAccessLevel()))
            return null;

        if (isEnabledValid(userToSave.getEnabled()))
            return null;

        userToSave.setCreatedTime(Timestamp.from(Instant.now()));
        userToSave.setLastModifiedTime(Timestamp.from(Instant.now()));
        userToSave.setEnabled(1);

        userToSave.setFirstName(userToSave.getFirstName().trim());
        userToSave.setLastName(userToSave.getLastName().trim());
        userToSave.setUsername(userToSave.getUsername().trim());
        userToSave.setPassword(userToSave.getPassword().trim());

        return repository.save(userToSave);
    }


    public User updateUser(String username, User updatedUser) {
        Optional<User> userOptional = repository.findById(username);

        if (userOptional.isEmpty())
            return null;

        if (isAccessLevelValid(updatedUser.getAccessLevel()))
            return null;

        if (isEnabledValid(updatedUser.getEnabled()))
            return null;


        User userToUpdate = userOptional.get();

        userToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        userToUpdate.setFirstName(updatedUser.getFirstName().trim());
        userToUpdate.setLastName(updatedUser.getLastName().trim());

        userToUpdate.setAccessLevel(updatedUser.getAccessLevel());
        userToUpdate.setEnabled(updatedUser.getEnabled());

        return repository.save(userToUpdate);
    }


    private boolean isAccessLevelValid(int accessLevel) {
        return (!(accessLevel == 1) && !(accessLevel == 0));
    }

    private boolean isEnabledValid(int enabled) {
        return (!(enabled == 1) && !(enabled == 0));
    }
}
