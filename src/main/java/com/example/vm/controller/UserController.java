package com.example.vm.controller;

import com.example.vm.model.User;
import com.example.vm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.findAll();

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> saveNewUser(@RequestBody User newUser) {
        newUser = userService.saveNewUser(newUser);

        if (newUser == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }


}
