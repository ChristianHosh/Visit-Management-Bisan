package com.example.vm.service;


import com.example.vm.controller.error.exception.EntityNotFoundException;
import com.example.vm.controller.error.exception.PasswordDoesntMatchException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.dto.post.UserPostDTO;
import com.example.vm.dto.put.UserPutDTO;
import com.example.vm.model.User;
import com.example.vm.repository.UserRepository;
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

    public ResponseEntity<List<User>> searchUsersByQuery(String query) {
        List<User> result = new ArrayList<>();

        List<User> list1 = repository.searchUsersByFirstNameContainingOrLastNameContainingOrUsernameContaining(query, query, query);
        List<User> list2 = new ArrayList<>();
        try {
            list2 = repository.searchUsersByAccessLevel(Integer.parseInt(query));
        } catch (NumberFormatException ignored) {
        }
        result.addAll(list1);
        result.addAll(list2);

        return ResponseEntity.ok(result);
    }

    public ResponseEntity<User> saveNewUser(UserPostDTO userRequest) {
        if (!userRequest.getConfirmPassword().equals(userRequest.getPassword()))
            throw new PasswordDoesntMatchException(PasswordDoesntMatchException.PASSWORD_DOES_NOT_MATCH);


        if (repository.findById(userRequest.getUsername()).isPresent())
            throw new UserAlreadyExistsException();


        User userToSave = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getConfirmPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .accessLevel(userRequest.getAccessLevel())
                .enabled(1)
                .build();

        userToSave = repository.save(userToSave);

        return ResponseEntity.ok(userToSave);
    }


    public ResponseEntity<User> updateUser(String username, UserPutDTO updatedDTO) {

        User userToUpdate = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        userToUpdate.setFirstName(updatedDTO.getFirstName());
        userToUpdate.setLastName(updatedDTO.getLastName());
        userToUpdate.setAccessLevel(updatedDTO.getAccessLevel());

        return ResponseEntity.ok(userToUpdate);
    }


    public ResponseEntity<User> enableUser(String username) {
        User user = repository.findById(username)
                .orElseThrow(() -> new EntityNotFoundException(EntityNotFoundException.USER_NOT_FOUND));

        user.setEnabled(user.getEnabled() == 0 ? 1 : 0);

        user = repository.save(user);

        return ResponseEntity.ok(user);
    }
}
