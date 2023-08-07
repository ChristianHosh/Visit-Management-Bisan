package com.example.vm.controller;

import com.example.vm.controller.error.exception.InvalidUserArgumentException;
import com.example.vm.controller.error.exception.UserAlreadyExistsException;
import com.example.vm.controller.error.exception.UserNotFoundException;
import com.example.vm.dto.UserRequestDTO;
import com.example.vm.model.User;
import com.example.vm.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@CrossOrigin
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
        if (User.isNotValidAccessLevel(accessLevel))
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
    public ResponseEntity<User> saveNewUser(@RequestBody @Valid UserRequestDTO userRequest) {
        User user = userService.findUserByUsername(userRequest.getUsername().trim());

        if (user != null) {
            throw new UserAlreadyExistsException("USERNAME ALREADY EXISTS!");
        }

        User savedUser = userService.saveNewUser(userRequest);

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
            if (User.isNotValidLength(string))
                throw new InvalidUserArgumentException("'" + string + "' IS TOO LONG, SHOULD BE LESS THAN 30 CHARACTERS");


        updatedUser = userService.updateUser(userToUpdate, updatedUser);

        if (updatedUser == null) {
            System.out.println("COULD NOT SAVE NEW USER");
            throw new RuntimeException("SOMETHING WRONG");
        }


        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @PutMapping("/{username}/disable")
    public ResponseEntity<User> disableUser(@PathVariable String username) {
        User userToDisable = userService.findUserByUsername(username);

        if (userToDisable == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");

        userToDisable = userService.disableUser(userToDisable);

        return new ResponseEntity<>(userToDisable, HttpStatus.OK);
    }

    @PutMapping("/{username}/enable")
    public ResponseEntity<User> enableUser(@PathVariable String username) {
        User userToEnable = userService.findUserByUsername(username);

        if (userToEnable == null)
            throw new UserNotFoundException("USERNAME NOT FOUND : '" + username + "'");

        userToEnable = userService.enableUser(userToEnable);

        return new ResponseEntity<>(userToEnable, HttpStatus.OK);
    }

    private void ValidateUser(User user) {
        if (User.isNotValidAccessLevel(user.getAccessLevel()))
            throw new InvalidUserArgumentException("ACCESS LEVEL IS NOT VALID, SHOULD BE A 1 OR 0");

        if (User.isNotValidEnabled(user.getEnabled()))
            throw new InvalidUserArgumentException("IS ENABLED IS NOT VALID, SHOULD BE A 1 OR 0");

        if (User.isNotValidName(user.getFirstName().trim()))
            throw new InvalidUserArgumentException("FIRST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");

        if (User.isNotValidName(user.getLastName().trim()))
            throw new InvalidUserArgumentException("LAST NAME IS NOT VALID, MUST CONTAIN CHARACTERS ONLY");
    }

}