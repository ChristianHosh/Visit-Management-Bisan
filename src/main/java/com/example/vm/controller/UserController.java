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
        List<User> userList = userService.searchUsersByFirstName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "lastName")
    public ResponseEntity<List<User>> searchByLastName(@RequestParam("lastName") String firstName) {
        List<User> userList = userService.searchUsersByLastName(firstName);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping(value = "/search", params = "accessLevel")
    public ResponseEntity<List<User>> searchByAccessLevel(@RequestParam("accessLevel") int accessLevel) {
        if (isNotValidAccessLevel(accessLevel))
            throw new InvalidUserArgumentException("ACCESS LEVEL IS NOT VALID, SHOULD BE A 1 OR 0");

        List<User> userList = userService.searchUsersByAccessLevel(accessLevel);

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

        ValidateUser(userToSave);
        // THROWS AN EXCEPTION IF VALIDATION FAILED

        for (String string : Arrays.asList(userToSave.getUsername(), userToSave.getPassword(), userToSave.getFirstName(), userToSave.getLastName()))
            if (isNotValidLength(string))
                throw new InvalidUserArgumentException("'" + string + "' IS TOO LONG, SHOULD BE LESS THAN 30 CHARACTERS");


        User savedUser = userService.saveNewUser(userToSave);

        if (savedUser == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            //TODO ADD BETTER EXCEPTIONS
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{username}")
    public ResponseEntity<User> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        User userToUpdate = userService.findUserByUsername(username);

        if (userToUpdate == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");

        ValidateUser(updatedUser);
        // THROWS AN EXCEPTION IF VALIDATION FAILED

        for (String string : Arrays.asList(updatedUser.getFirstName(), updatedUser.getLastName()))
            if (isNotValidLength(string))
                throw new InvalidUserArgumentException("'" + string + "' IS TOO LONG, SHOULD BE LESS THAN 30 CHARACTERS");


        updatedUser = userService.updateUser(userToUpdate, updatedUser);

        if (updatedUser == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<User> disableUser(@PathVariable String username) {
        User userToDelete = userService.findUserByUsername(username);

        if (userToDelete == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");

        userToDelete= userService.disableUser(userToDelete);

        return new ResponseEntity<>(userToDelete, HttpStatus.OK);
    }


    private void ValidateUser(User user) {
        if (isNotValidAccessLevel(user.getAccessLevel()))
            throw new InvalidUserArgumentException("ACCESS LEVEL IS NOT VALID, SHOULD BE A 1 OR 0");

        if (isNotValidEnabled(user.getEnabled()))
            throw new InvalidUserArgumentException("IS ENABLED IS NOT VALID, SHOULD BE A 1 OR 0");

        if (isNotValidName(user.getFirstName().trim()))
            throw new InvalidUserArgumentException("FIRST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        if (isNotValidName(user.getLastName().trim()))
            throw new InvalidUserArgumentException("LAST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");
    }

    private boolean isNotValidAccessLevel(int accessLevel) {
        return (accessLevel != 1) && (accessLevel != 0);
    }

    private boolean isNotValidEnabled(int enabled) {
        return (enabled != 1) && (enabled != 0);
    }

    public boolean isNotValidName(String name) {
        return !name.matches("[a-zA-Z ]+");
    }

    public boolean isNotValidLength(String string) {
        return string.length() > 30;
    }

}