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

    public List<User> findAll(){
        return repository.findAll();
    }

    public User findByUsername(String username){
        Optional<User> userOptional = repository.findById(username);

        return userOptional.orElse(null);
    }

    public User saveNewUser(User userToSave){
        Optional<User> userOptional = repository.findById(userToSave.getUsername());

        if (userOptional.isPresent()) return null;


        userToSave.setCreatedTime(Timestamp.from(Instant.now()));
        userToSave.setLastModifiedTime(Timestamp.from(Instant.now()));

        return repository.save(userToSave);
    }

}
