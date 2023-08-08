package com.example.vm.service;


import com.example.vm.dto.post.UserPostDTO;
import com.example.vm.dto.put.UserPutDTO;
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


    public User saveNewUser(UserPostDTO userRequest) {
        Timestamp timestamp = Timestamp.from(Instant.now());

        User userToSave = User.builder()
                .username(userRequest.getUsername())
                .password(userRequest.getPassword())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .accessLevel(userRequest.getAccessLevel())
                .enabled(1)
                .build();

        userToSave.setCreatedTime(timestamp);
        userToSave.setLastModifiedTime(timestamp);

        return repository.save(userToSave);
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
