package com.example.vm.controller;

import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.model.User;
import com.example.vm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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
        List<User> userList = userService.findAllUsers();

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "firstName")
    public ResponseEntity<List<User>> searchByFirstName(@RequestParam("firstName") String firstName) {
        System.out.println("first");
        List<User> userList = userService.searchUsersByFirstName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<User>> searchByLastName(@RequestParam("lastName") String firstName) {
        System.out.println("last");
        List<User> userList = userService.searchUsersByLastName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "accessLevel")
    public ResponseEntity<List<User>> searchByAccessLevel(@RequestParam("accessLevel") int accessLevel) {
        List<User> userList = userService.searchUsersByAccessLevel(accessLevel);

        if (userList == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);

        if (user == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");


        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<User> saveNewUser(@RequestBody User userToSave) {
        User user = userService.findUserByUsername(userToSave.getUsername().trim());

        if (user != null) {
            throw new UserAlreadyExistsException("USERNAME ALREADY EXISTS!");
        }

        if (isAccessLevelNotValid(userToSave.getAccessLevel()))
            throw new InvalidUserArgumentException("ACCESS LEVEL IS NOT VALID, SHOULD BE A 1 OR 0");

        if (isEnabledNotValid(userToSave.getEnabled()))
            throw new InvalidUserArgumentException("IS ENABLED IS NOT VALID, SHOULD BE A 1 OR 0");

        if (isNameNotValid(userToSave.getFirstName().trim()))
            throw new InvalidUserArgumentException("FIRST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        if (isNameNotValid(userToSave.getLastName().trim()))
            throw new InvalidUserArgumentException("LAST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        for (String string : Arrays.asList(userToSave.getUsername(), userToSave.getPassword(), userToSave.getFirstName(), userToSave.getLastName()))
            if (!isValidLength(string))
                throw new InvalidUserArgumentException("'" + string + "' IS TOO LONG, SHOULD BE LESS THAN 30 CHARACTERS");


        userToSave = userService.saveNewUser(userToSave);

        if (userToSave == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(userToSave, HttpStatus.CREATED);
    }

//    @PutMapping("/{username}")
//    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User userToUpdate) {
//        userToUpdate = userService.updateUser(username, userToUpdate);
//
//        if (userToUpdate == null) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//
//        return new ResponseEntity<>(userToUpdate, HttpStatus.OK);
//    }

    private boolean isAccessLevelNotValid(int accessLevel) {
        return (accessLevel != 1) && (accessLevel != 0);
    }

    private boolean isEnabledNotValid(int enabled) {
        return (enabled != 1) && (enabled != 0);
    }

    public boolean isNameNotValid(String name) {
        return !name.matches("[a-zA-Z ]+");
    }

    public boolean isValidLength(String string) {
        return string.length() <= 30;
    }

}