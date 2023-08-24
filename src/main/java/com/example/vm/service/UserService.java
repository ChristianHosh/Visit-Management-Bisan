package com.example.vm.service;


import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.PasswordDoesntMatchException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.dto.request.UserPostRequest;
import com.example.vm.dto.request.UserRequest;
import com.example.vm.model.User;
import com.example.vm.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        return ResponseEntity.ok(foundUser);
    }
    public ResponseEntity<List<User>> findEmployeeUsers() {
        return ResponseEntity.ok(repository.searchUsersByAccessLevelAndEnabled(0,1));
    }

    public ResponseEntity<List<User>> searchUsersByQuery(String query) {
        List<User> result = new ArrayList<>();

        List<User> list1 = repository.searchUsersByFirstNameContainingOrLastNameContainingOrUsernameContaining(query, query, query);
        List<User> list2 = new ArrayList<>();

        try {
            list2 = repository.searchUsersByAccessLevel(Integer.parseInt(query));
        } catch (NumberFormatException ignored) {}

        result.addAll(list1);
        result.addAll(list2);

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<User> saveNewUser(@Valid UserPostRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword()))
            throw new PasswordDoesntMatchException(PasswordDoesntMatchException.PASSWORD_DOES_NOT_MATCH);

        if (repository.existsById(userRequest.getUsername()))
            throw new UserAlreadyExistsException();

        User userToSave = User.builder()
                .username(userRequest.getUsername().toLowerCase())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(userRequest.getPassword())
                .accessLevel(userRequest.getAccessLevel())
                .build();

        userToSave = repository.save(userToSave);

        return ResponseEntity.ok(userToSave);
    }


    public ResponseEntity<User> updateUser(String username, UserRequest updatedDTO) {
        User userToUpdate = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        userToUpdate.setFirstName(updatedDTO.getFirstName());
        userToUpdate.setLastName(updatedDTO.getLastName());
        userToUpdate.setAccessLevel(updatedDTO.getAccessLevel());

        userToUpdate = repository.save(userToUpdate);

        return ResponseEntity.ok(userToUpdate);
    }


    public ResponseEntity<User> enableUser(String username) {
        User foundUser = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        foundUser.setEnabled(!foundUser.getEnabled());

        foundUser = repository.save(foundUser);

        return ResponseEntity.ok(foundUser);
    }
}
