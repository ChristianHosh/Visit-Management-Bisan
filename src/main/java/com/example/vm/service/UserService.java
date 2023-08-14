package com.example.vm.service;


import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.post.UserPostDTO;
import com.example.vm.dto.put.UserPutDTO;
import com.example.vm.model.User;
import com.example.vm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
public class UserService {

    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public ResponseEntity<List<User>> findAllUsers() {
        return ResponseEntity.ok(repository.findAll());
    }

    public ResponseEntity<User> findUserByUsername(String username) {
        User foundUser = repository.findById(username)
                .orElseThrow( () -> new UserNotFoundException(UserNotFoundException.USER_NOT_FOUND));

        return ResponseEntity.ok(foundUser);
    }


    public ResponseEntity<List<User>> searchUsersByFirstName(String firstName) {
        return ResponseEntity.ok(repository.searchUsersByFirstNameContaining(firstName));
    }

    public ResponseEntity<List<User>> searchUsersByLastName(String lastName) {
        return ResponseEntity.ok(repository.searchUsersByLastNameContaining(lastName));
    }

    public ResponseEntity<List<User>> searchUsersByAccessLevel(int accessLevel) {
        return ResponseEntity.ok(repository.searchUsersByAccessLevel(accessLevel));
    }


    public ResponseEntity<?> saveNewUser(UserPostDTO userRequest) {
//        VALIDATE PASSWORD

        Timestamp timestamp = Timestamp.from(Instant.now());

        User userToSave = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getConfirmPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .accessLevel(userRequest.getAccessLevel())
                .enabled(1)
                .build();

        userToSave.setCreatedTime(timestamp);
        userToSave.setLastModifiedTime(timestamp);

//        return repository.save(userToSave);
        return ResponseEntity.ok(userToSave);
    }


    public User updateUser(User userToUpdate, UserPutDTO updatedDTO) {

        userToUpdate.setLastModifiedTime(Timestamp.from(Instant.now()));

        userToUpdate.setFirstName(updatedDTO.getFirstName() == null ? userToUpdate.getFirstName() : updatedDTO.getFirstName());
        userToUpdate.setLastName(updatedDTO.getLastName() == null ? userToUpdate.getLastName() : updatedDTO.getLastName());
        userToUpdate.setAccessLevel(updatedDTO.getAccessLevel() == null ? userToUpdate.getAccessLevel() : updatedDTO.getAccessLevel());

        return repository.save(userToUpdate);
    }



    public User enableUser(User user) {
        user.setEnabled(user.getEnabled() == 0 ? 1 : 0);
        return repository.save(user);
    }
}
